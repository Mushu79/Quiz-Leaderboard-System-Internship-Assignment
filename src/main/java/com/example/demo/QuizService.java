package com.example.demo;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service
public class QuizService {

    private static final String BASE_URL =
            "https://devapigw.vidalhealthtpa.com/srm-quiz-task/quiz/messages?regNo=2024CS101&poll=";

    public String processQuiz() throws Exception {

        Set<String> seen = new HashSet<>();
        Map<String, Integer> scores = new HashMap<>();

        for (int i = 0; i < 10; i++) {

            String response = getAPI(BASE_URL + i);

            JSONObject json = new JSONObject(response);
            JSONArray events = json.getJSONArray("events");

            for (int j = 0; j < events.length(); j++) {

                JSONObject event = events.getJSONObject(j);

                String roundId = event.getString("roundId");
                String participant = event.getString("participant");
                int score = event.getInt("score");

                String key = roundId + "_" + participant;

                if (!seen.contains(key)) {
                    seen.add(key);
                    scores.put(participant,
                            scores.getOrDefault(participant, 0) + score);
                }
            }

            Thread.sleep(5000);
        }

        List<Map.Entry<String, Integer>> list = new ArrayList<>(scores.entrySet());
        list.sort((a, b) -> b.getValue() - a.getValue());

        JSONArray leaderboard = new JSONArray();
        int total = 0;

        for (Map.Entry<String, Integer> entry : list) {

            JSONObject obj = new JSONObject();
            obj.put("participant", entry.getKey());
            obj.put("totalScore", entry.getValue());

            total += entry.getValue();
            leaderboard.put(obj);
        }

        JSONObject submit = new JSONObject();
        submit.put("regNo", "2024CS101"); // change to your reg no
        submit.put("leaderboard", leaderboard);

        return postAPI(
                "https://devapigw.vidalhealthtpa.com/srm-quiz-task/quiz/submit",
                submit.toString()
        );
    }

    private String getAPI(String urlStr) throws Exception {

        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream())
        );

        String line;
        StringBuilder response = new StringBuilder();

        while ((line = br.readLine()) != null) {
            response.append(line);
        }

        br.close();
        return response.toString();
    }

    private String postAPI(String urlStr, String jsonInput) throws Exception {

        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        con.getOutputStream().write(jsonInput.getBytes());

        BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream())
        );

        String line;
        StringBuilder response = new StringBuilder();

        while ((line = br.readLine()) != null) {
            response.append(line);
        }

        br.close();
        return response.toString();
    }
}
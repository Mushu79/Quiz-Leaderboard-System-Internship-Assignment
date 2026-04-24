# Quiz Leaderboard System — Bajaj Finserv Health Internship

## Project Overview
This application is a backend integration system designed to consume, process, and aggregate quiz data from a validator API. It handles challenges related to distributed systems, specifically data deduplication.

## Core Features
* [cite_start]**Iterative Polling**: Executes 10 consecutive API calls (poll 0-9) to gather all event data[cite: 11, 22].
* [cite_start]**Rate Limiting**: Implements a mandatory 5-second delay between each poll to satisfy API constraints[cite: 23].
* [cite_start]**Data Deduplication**: Uses a composite key `(roundId + participant)` to ensure each scoring event is only counted once [cite: 13, 63-66].
* [cite_start]**Leaderboard Generation**: Aggregates unique scores and sorts participants by `totalScore`[cite: 14, 15].

## Technical Implementation
* **Language**: Java
* **Logic**:
  1. [cite_start]Poll `GET /quiz/messages?regNo=AP23110010399&poll={0-9}`[cite: 20, 21].
  2. [cite_start]Store `roundId + participant` in a `HashSet` to identify duplicates [cite: 13, 56-58].
  3. Aggregate scores in a `HashMap`.
  4. [cite_start]Submit final leaderboard via `POST /quiz/submit` [cite: 38-47].

## How to Run
1. Clone the repository: `git clone https://github.com/Mushu79/Quiz-Leaderboard-System-Internship-Assignment.git`
2. Build the project: `./mvnw clean install`
3. Run the application: `java -jar target/quiz-system.jar`

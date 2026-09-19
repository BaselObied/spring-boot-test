package com.fawry.quizarena.web.dto;

import java.io.Serializable;

public class LeaderboardEntry implements Serializable {

    private final String username;
    private final int totalScore;

    public LeaderboardEntry(String username, int totalScore) {
        this.username = username;
        this.totalScore = totalScore;
    }

    public String getUsername() {
        return username;
    }

    public int getTotalScore() {
        return totalScore;
    }
}
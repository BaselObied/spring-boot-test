package com.fawry.quizarena.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private int totalScore = 0;

    protected Player() {
        // JPA
    }

    public Player(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void addScore(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("points cannot be negative");
        }
        this.totalScore += points;
    }
}

package com.fawry.quizarena.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id")
    private Player player;

    @ElementCollection
    @CollectionTable(name = "match_questions", joinColumns = @JoinColumn(name = "match_id"))
    @Column(name = "question_id")
    @OrderColumn(name = "position")
    private List<Long> questionIds = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchStatus status = MatchStatus.IN_PROGRESS;

    private int currentQuestionIndex = 0;

    private int score = 0;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    protected Match() {
        // JPA
    }

    public Match(Player player, List<Long> questionIds) {
        this.player = player;
        this.questionIds = questionIds;
        this.startedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public List<Long> getQuestionIds() {
        return questionIds;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public int getScore() {
        return score;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public Long currentQuestionId() {
        assertInProgress();
        if (currentQuestionIndex >= questionIds.size()) {
            throw new IllegalStateException("no more questions in match " + id);
        }
        return questionIds.get(currentQuestionIndex);
    }

    public boolean hasMoreQuestions() {
        return currentQuestionIndex < questionIds.size();
    }

    public void registerAnswer(boolean correct, int pointsForCorrectAnswer) {
        assertInProgress();
        if (correct) {
            score += pointsForCorrectAnswer;
        }
        currentQuestionIndex++;
        if (!hasMoreQuestions()) {
            finish();
        }
    }

    public void finish() {
        if (status == MatchStatus.FINISHED) {
            return;
        }
        status = MatchStatus.FINISHED;
        finishedAt = LocalDateTime.now();
    }

    private void assertInProgress() {
        if (status != MatchStatus.IN_PROGRESS) {
            throw new IllegalStateException("match " + id + " is already finished");
        }
    }
}

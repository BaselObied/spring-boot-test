package com.fawry.quizarena.web.dto;

import com.fawry.quizarena.domain.Match;

public record MatchResponse(
        Long id,
        Long playerId,
        String status,
        int score,
        Long currentQuestionId
) {
    public static MatchResponse from(Match match) {
        Long currentQuestionId = match.hasMoreQuestions() ? match.currentQuestionId() : null;
        return new MatchResponse(
                match.getId(),
                match.getPlayer().getId(),
                match.getStatus().name(),
                match.getScore(),
                currentQuestionId
        );
    }
}

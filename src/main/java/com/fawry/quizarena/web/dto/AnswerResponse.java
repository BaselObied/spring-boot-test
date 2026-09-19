package com.fawry.quizarena.web.dto;

import com.fawry.quizarena.service.AnswerResult;

public record AnswerResponse(
        boolean correct,
        int currentScore,
        boolean matchFinished,
        Long nextQuestionId
) {
    public static AnswerResponse from(AnswerResult result) {
        return new AnswerResponse(
                result.correct(),
                result.currentScore(),
                result.matchFinished(),
                result.nextQuestionId()
        );
    }
}
package com.fawry.quizarena.service;

public record AnswerResult(
        boolean correct,
        int currentScore,
        boolean matchFinished,
        Long nextQuestionId
) {
}
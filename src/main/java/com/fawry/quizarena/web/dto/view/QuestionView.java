package com.fawry.quizarena.web.dto.view;

import com.fawry.quizarena.domain.Question;

import java.util.List;

public record QuestionView(String text, List<String> options) {

    public static QuestionView from(Question question) {
        return new QuestionView(question.getText(), question.getOptions());
    }
}

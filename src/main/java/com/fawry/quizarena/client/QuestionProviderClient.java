package com.fawry.quizarena.client;

import com.fawry.quizarena.domain.Question;

import java.util.List;

public interface QuestionProviderClient {
    List<Question> fetchQuestions(int amount, String category);
}

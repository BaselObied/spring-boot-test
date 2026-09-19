package com.fawry.quizarena.client.impl;

import com.fawry.quizarena.client.QuestionProviderClient;
import com.fawry.quizarena.domain.Question;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Fixed, in-memory question set — no network call at all.
 * Active only when the "mock" profile is on, so local/dev runs
 * never depend on the trivia provider being up.
 */
@Component
@Profile("stub")
public class MockQuestionProviderClient implements QuestionProviderClient {

    @Override
    public List<Question> fetchQuestions(int amount, String category) {
        List<Question> pool = List.of(
                new Question("What is the capital of France?",
                        List.of("Paris", "Berlin", "Madrid", "Rome"), "Paris", category),
                new Question("Which planet is known as the Red Planet?",
                        List.of("Venus", "Mars", "Jupiter", "Saturn"), "Mars", category),
                new Question("What is 7 x 8?",
                        List.of("54", "56", "58", "64"), "56", category),
                new Question("Who wrote 'Romeo and Juliet'?",
                        List.of("Dickens", "Shakespeare", "Austen", "Hemingway"), "Shakespeare", category),
                new Question("What is the largest ocean on Earth?",
                        List.of("Atlantic", "Indian", "Arctic", "Pacific"), "Pacific", category),
                new Question("How many continents are there?",
                        List.of("5", "6", "7", "8"), "7", category)
        );

        List<Question> result = new ArrayList<>(pool);
        int size = Math.min(amount, result.size());
        return result.subList(0, size);
    }
}
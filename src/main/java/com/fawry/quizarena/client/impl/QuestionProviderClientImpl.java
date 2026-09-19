package com.fawry.quizarena.client.impl;


import com.fawry.quizarena.client.QuestionProviderClient;
import com.fawry.quizarena.client.TriviaApiResponse;
import com.fawry.quizarena.domain.Question;
import com.fawry.quizarena.exception.QuestionProviderException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Talks to an external trivia question provider over REST.
 */
@Component
@Profile("trivia")
@Primary
public class QuestionProviderClientImpl implements QuestionProviderClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public QuestionProviderClientImpl(RestTemplate restTemplate,
                                      @Value("${quizarena.trivia-api.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @Override
    public List<Question> fetchQuestions(int amount, String category) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("amount", amount)
                .queryParam("category", category)
                .queryParam("type", "multiple")
                .toUriString();

        TriviaApiResponse response;
        try {
            response = restTemplate.getForObject(url, TriviaApiResponse.class);
        } catch (RestClientException ex) {
            throw new QuestionProviderException("failed to fetch questions from trivia provider", ex);
        }

        if (response == null || response.getResults() == null) {
            throw new QuestionProviderException("trivia provider returned an empty response", null);
        }

        List<Question> questions = new ArrayList<>();
        for (TriviaApiResponse.TriviaQuestionDto dto : response.getResults()) {
            List<String> options = new ArrayList<>(dto.getIncorrectAnswers());
            options.add(dto.getCorrectAnswer());
            questions.add(new Question(dto.getQuestion(), options, dto.getCorrectAnswer(), dto.getCategory()));
        }
        return questions;
    }
}
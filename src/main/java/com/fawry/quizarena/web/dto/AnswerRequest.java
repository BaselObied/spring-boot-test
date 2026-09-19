package com.fawry.quizarena.web.dto;

import jakarta.validation.constraints.NotBlank;

public record AnswerRequest(@NotBlank String answer) {
}

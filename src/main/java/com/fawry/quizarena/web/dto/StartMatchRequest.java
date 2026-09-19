package com.fawry.quizarena.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StartMatchRequest(
        @NotNull Long playerId,
        @NotBlank String category
) {
}
package com.fawry.quizarena.web.dto.view;

import com.fawry.quizarena.domain.Match;
import com.fawry.quizarena.domain.MatchStatus;

public record MatchView(Long id, int score, boolean finished) {

    public static MatchView from(Match match) {
        return new MatchView(match.getId(), match.getScore(), match.getStatus() == MatchStatus.FINISHED);
    }
}

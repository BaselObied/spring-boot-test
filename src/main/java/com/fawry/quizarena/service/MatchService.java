package com.fawry.quizarena.service;

import com.fawry.quizarena.domain.Match;
import com.fawry.quizarena.web.dto.view.MatchDetailsView;

public interface MatchService {
    Match startMatch(Long playerId, String category);
    AnswerResult submitAnswer(Long matchId, String submittedAnswer);
    MatchDetailsView getMatchDetails(Long matchId);
}

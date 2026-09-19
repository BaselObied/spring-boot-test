package com.fawry.quizarena.service.impl;

import com.fawry.quizarena.client.QuestionProviderClient;
import com.fawry.quizarena.domain.Match;
import com.fawry.quizarena.domain.MatchStatus;
import com.fawry.quizarena.domain.Player;
import com.fawry.quizarena.domain.Question;
import com.fawry.quizarena.exception.ResourceNotFoundException;
import com.fawry.quizarena.repository.MatchRepository;
import com.fawry.quizarena.repository.PlayerRepository;
import com.fawry.quizarena.repository.QuestionRepository;
import com.fawry.quizarena.service.AnswerResult;
import com.fawry.quizarena.service.LeaderboardService;
import com.fawry.quizarena.service.MatchService;
import com.fawry.quizarena.web.dto.view.MatchDetailsView;
import com.fawry.quizarena.web.dto.view.MatchView;
import com.fawry.quizarena.web.dto.view.QuestionView;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MatchServiceImpl implements MatchService {

    private static final int QUESTIONS_PER_MATCH = 5;
    private static final int POINTS_PER_CORRECT_ANSWER = 10;

    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;
    private final QuestionRepository questionRepository;
    private final QuestionProviderClient questionProviderClient;
    private final LeaderboardService leaderboardService;

    public MatchServiceImpl(MatchRepository matchRepository,
                        PlayerRepository playerRepository,
                        QuestionRepository questionRepository,
                        QuestionProviderClient questionProviderClient,
                        LeaderboardService leaderboardService) {
        this.matchRepository = matchRepository;
        this.playerRepository = playerRepository;
        this.questionRepository = questionRepository;
        this.questionProviderClient = questionProviderClient;
        this.leaderboardService = leaderboardService;
    }

    @Transactional
    public Match startMatch(Long playerId, String category) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("no player with id " + playerId));

        List<Question> questions = questionProviderClient.fetchQuestions(QUESTIONS_PER_MATCH, category);
        List<Question> saved = questionRepository.saveAll(questions);
        List<Long> questionIds = saved.stream().map(Question::getId).toList();

        Match match = new Match(player, questionIds);
        return matchRepository.save(match);
    }

    @Transactional
    public AnswerResult submitAnswer(Long matchId, String submittedAnswer) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("no match with id " + matchId));

        Long questionId = match.currentQuestionId();
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("no question with id " + questionId));

        boolean correct = question.isCorrect(submittedAnswer);
        match.registerAnswer(correct, POINTS_PER_CORRECT_ANSWER);

        boolean finished = match.getStatus() == MatchStatus.FINISHED;
        if (finished) {
            finalizeMatch(match);
        }

        Long nextQuestionId = match.hasMoreQuestions() ? match.currentQuestionId() : null;
        return new AnswerResult(correct, match.getScore(), finished, nextQuestionId);
    }

    @Transactional(readOnly = true)
    @Override
    public MatchDetailsView getMatchDetails(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("no match with id " + matchId));

        QuestionView questionView = null;
        if (match.hasMoreQuestions()) {
            Question question = questionRepository.findById(match.currentQuestionId())
                    .orElseThrow(() -> new ResourceNotFoundException("no question with id " + match.currentQuestionId()));
            questionView = QuestionView.from(question);
        }

        return new MatchDetailsView(MatchView.from(match), questionView);
    }

    private void finalizeMatch(Match match) {
        Player player = match.getPlayer();
        player.addScore(match.getScore());
        playerRepository.save(player);
        leaderboardService.invalidate();
    }
}
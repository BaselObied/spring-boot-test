package com.fawry.quizarena.web.controller;

import com.fawry.quizarena.web.dto.AnswerRequest;
import com.fawry.quizarena.web.dto.AnswerResponse;
import com.fawry.quizarena.web.dto.MatchResponse;
import com.fawry.quizarena.web.dto.StartMatchRequest;
import com.fawry.quizarena.domain.Match;
import com.fawry.quizarena.service.AnswerResult;
import com.fawry.quizarena.service.MatchService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping
    public ResponseEntity<MatchResponse> startMatch(@Valid @RequestBody StartMatchRequest request) {
        Match match = matchService.startMatch(request.playerId(), request.category());
        return ResponseEntity.status(HttpStatus.CREATED).body(MatchResponse.from(match));
    }

    @PostMapping("/{matchId}/answers")
    public ResponseEntity<AnswerResponse> submitAnswer(@PathVariable Long matchId,
                                                       @Valid @RequestBody AnswerRequest request) {
        AnswerResult result = matchService.submitAnswer(matchId, request.answer());
        return ResponseEntity.ok(AnswerResponse.from(result));
    }
}

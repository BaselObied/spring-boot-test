package com.fawry.quizarena.web.controller;

import com.fawry.quizarena.domain.Match;
import com.fawry.quizarena.domain.Player;
import com.fawry.quizarena.domain.Question;
import com.fawry.quizarena.exception.QuestionProviderException;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Server-rendered Thymeleaf UI, kept separate from the JSON REST API
 * in MatchController/PlayerController/LeaderboardController so neither
 * side has to compromise its response shape for the other.
 */
@Controller
public class GameViewController {

    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final QuestionRepository questionRepository;
    private final MatchService matchService;
    private final LeaderboardService leaderboardService;

    public GameViewController(PlayerRepository playerRepository,
                              MatchRepository matchRepository,
                              QuestionRepository questionRepository,
                              MatchService matchService,
                              LeaderboardService leaderboardService) {
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
        this.questionRepository = questionRepository;
        this.matchService = matchService;
        this.leaderboardService = leaderboardService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/ui/players")
    public String registerPlayer(@RequestParam String username, RedirectAttributes redirectAttributes) {
        Player player = playerRepository.save(new Player(username));
        redirectAttributes.addFlashAttribute("message", "Registered '" + player.getUsername() + "' as player id " + player.getId());
        return "redirect:/";
    }

    @PostMapping("/ui/matches")
    public String startMatch(@RequestParam Long playerId,
                             @RequestParam String category,
                             RedirectAttributes redirectAttributes) {
        try {
            Match match = matchService.startMatch(playerId, category);
            return "redirect:/ui/matches/" + match.getId();
        } catch (ResourceNotFoundException | QuestionProviderException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/ui/matches/{id}")
    public String viewMatch(@PathVariable Long id, Model model) {
        MatchDetailsView details = matchService.getMatchDetails(id);
        model.addAttribute("match", details.match());
        if (details.question() != null) {
            model.addAttribute("question", details.question());
        }
        return "match";
    }

    @PostMapping("/ui/matches/{id}/answers")
    public String submitAnswer(@PathVariable Long id,
                               @RequestParam String answer,
                               RedirectAttributes redirectAttributes) {
        AnswerResult result = matchService.submitAnswer(id, answer);
        redirectAttributes.addFlashAttribute("lastAnswerCorrect", result.correct());
        return "redirect:/ui/matches/" + id;
    }

    @GetMapping("/ui/leaderboard")
    public String leaderboard(Model model) {
        model.addAttribute("entries", leaderboardService.topPlayers());
        return "leaderboard";
    }
}

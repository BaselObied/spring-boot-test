package com.fawry.quizarena.web.controller;


import com.fawry.quizarena.domain.Player;
import com.fawry.quizarena.exception.ResourceNotFoundException;
import com.fawry.quizarena.repository.PlayerRepository;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerRepository playerRepository;

    public PlayerController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Player register(@RequestBody @NotBlank String username) {
        Player player = new Player(username);
        return playerRepository.save(player);
    }

    @GetMapping("/{id}")
    public Player get(@PathVariable Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("no player with id " + id));
    }
}

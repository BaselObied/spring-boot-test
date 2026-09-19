package com.fawry.quizarena.service.impl;

import com.fawry.quizarena.web.dto.LeaderboardEntry;
import com.fawry.quizarena.repository.PlayerRepository;
import com.fawry.quizarena.service.LeaderboardService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaderboardServiceImpl implements LeaderboardService {

    public static final String LEADERBOARD_CACHE = "leaderboard";
    public static final String TOP_10_KEY = "'top10'";

    private final PlayerRepository playerRepository;

    public LeaderboardServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * Recomputing top-10 on every request means sorting the whole players
     * table each time. Cache it, and evict whenever a score changes.
     */
    @Cacheable(value = LEADERBOARD_CACHE, key = TOP_10_KEY)
    @Override
    public List<LeaderboardEntry> topPlayers() {
        return playerRepository.findTop10ByOrderByTotalScoreDesc()
                .stream()
                .map(p -> new LeaderboardEntry(p.getUsername(), p.getTotalScore()))
                .toList();
    }

    @CacheEvict(value = LEADERBOARD_CACHE, key = TOP_10_KEY)
    @Override
    public void invalidate() {
        // body intentionally empty — annotation does the work
    }
}

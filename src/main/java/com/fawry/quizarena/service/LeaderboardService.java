package com.fawry.quizarena.service;

import com.fawry.quizarena.web.dto.LeaderboardEntry;

import java.util.List;

public interface LeaderboardService {
    List<LeaderboardEntry> topPlayers();
    void invalidate();
}

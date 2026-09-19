package com.fawry.quizarena.repository;

import com.fawry.quizarena.domain.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
}
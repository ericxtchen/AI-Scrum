package com.ericxtchen.aiscrum.repositories;

import com.ericxtchen.aiscrum.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findTeamByTeamName(String teamName);
}

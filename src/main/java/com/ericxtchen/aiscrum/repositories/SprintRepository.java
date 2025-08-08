package com.ericxtchen.aiscrum.repositories;
import com.ericxtchen.aiscrum.entities.Sprint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Long> {
    Optional<Sprint> getSprintBySprintName(String sprintName);
}

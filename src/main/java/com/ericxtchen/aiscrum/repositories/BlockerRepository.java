package com.ericxtchen.aiscrum.repositories;

import com.ericxtchen.aiscrum.entities.Blocker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlockerRepository extends JpaRepository<Blocker, Long> {
    Optional<Blocker> findBySprint_Id(Long sprintId);
}

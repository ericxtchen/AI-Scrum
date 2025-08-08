package com.ericxtchen.aiscrum.repositories;

import com.ericxtchen.aiscrum.entities.Retrospective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RetrospectiveRepository extends JpaRepository<Retrospective,Long> {
}

package com.ericxtchen.aiscrum.repositories;

import com.ericxtchen.aiscrum.entities.MetricSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetricSnapshotRepository extends JpaRepository<MetricSnapshot,Long> {
    public Optional<MetricSnapshot> findFirstByTeamIdOrderByTimestampDesc(Long teamId);
}

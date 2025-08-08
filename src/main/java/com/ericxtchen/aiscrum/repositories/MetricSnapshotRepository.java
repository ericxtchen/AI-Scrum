package com.ericxtchen.aiscrum.repositories;

import com.ericxtchen.aiscrum.entities.MetricSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricSnapshotRepository extends JpaRepository<MetricSnapshot,Long> {
}

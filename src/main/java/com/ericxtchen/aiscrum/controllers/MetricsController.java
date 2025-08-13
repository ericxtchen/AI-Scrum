package com.ericxtchen.aiscrum.controllers;

import com.ericxtchen.aiscrum.entities.MetricSnapshot;
import com.ericxtchen.aiscrum.repositories.MetricSnapshotRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/metrics")
public class MetricsController {
    public final MetricSnapshotRepository metricSnapshotRepository;
    public MetricsController(MetricSnapshotRepository metricSnapshotRepository) {
        this.metricSnapshotRepository = metricSnapshotRepository;
    }

    @GetMapping("/teams/{teamId}/average-velocity")
    public ResponseEntity<MetricSnapshot> getAverageVelocity(@PathVariable Long teamId) {
        Optional<MetricSnapshot> metric = metricSnapshotRepository.findFirstByTeamIdOrderByTimestampDesc(teamId);
        return metric.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

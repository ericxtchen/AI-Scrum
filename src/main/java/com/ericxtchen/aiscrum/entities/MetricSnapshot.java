package com.ericxtchen.aiscrum.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "metric_snapshots")
public class MetricSnapshot {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(nullable = false)
    private String metricName;

    @Getter
    @Setter
    @Column(nullable = false)
    private String metricValue;

    @Getter
    @Setter
    @Column(nullable = false)
    private OffsetDateTime timestamp;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_id", nullable = true)
    private Sprint sprint;
}

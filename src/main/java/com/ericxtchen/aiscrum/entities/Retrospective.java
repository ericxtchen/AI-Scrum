package com.ericxtchen.aiscrum.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "retrospectives")
public class Retrospective {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(nullable = false)
    private LocalDate date;

    @Getter
    @Setter
    @Lob
    private String positiveSummary;

    @Getter
    @Setter
    @Lob
    private String improvementSummary;

    @Getter
    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_id", unique = true)
    private Sprint sprint;
}

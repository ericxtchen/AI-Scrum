package com.ericxtchen.aiscrum.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Blocker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_id", nullable = false)
    @Getter
    @Setter
    private Sprint sprint;

    @Getter
    @Setter
    private String description;
    @Getter
    @Setter
    private String status = "ACTIVE"; // e.g., ACTIVE, RESOLVED

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private MeetingSummary meetingSummary;
}

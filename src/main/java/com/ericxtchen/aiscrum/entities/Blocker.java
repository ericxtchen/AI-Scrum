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

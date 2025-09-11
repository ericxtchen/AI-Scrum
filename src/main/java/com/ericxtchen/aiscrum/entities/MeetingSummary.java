package com.ericxtchen.aiscrum.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class MeetingSummary {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_id", nullable = false)
    @Getter
    @Setter
    private Sprint sprint;

    @Getter
    @Setter
    private LocalDate meetingDate;

    @Getter
    @Setter
    @Lob // For potentially long text
    private String summaryText;

    @Getter
    @Setter
    @OneToMany(mappedBy = "meetingSummary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Blocker> blockers = new ArrayList<>();
}

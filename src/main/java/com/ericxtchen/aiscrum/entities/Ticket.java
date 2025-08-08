package com.ericxtchen.aiscrum.entities;
import com.ericxtchen.aiscrum.enums.TicketStatus;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(nullable = false)
    private String externalId; // JIRA ID

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @Getter
    @Setter
    private int storyPoints;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_id", nullable = false)
    @Getter
    @Setter
    private Sprint sprint;


}

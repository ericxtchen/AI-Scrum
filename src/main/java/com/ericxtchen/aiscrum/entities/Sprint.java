package com.ericxtchen.aiscrum.entities;
import com.ericxtchen.aiscrum.enums.SprintStatus;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sprints")
public class Sprint {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(name = "sprint_name", nullable = false)
    private String sprintName;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Getter
    @Setter
    @OneToMany(
            mappedBy = "sprint",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Ticket> tasks = new ArrayList<>();

    @Getter
    @Setter
    private LocalDate startDate;

    @Getter
    @Setter
    private LocalDate endDate;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private SprintStatus sprintStatus;

}

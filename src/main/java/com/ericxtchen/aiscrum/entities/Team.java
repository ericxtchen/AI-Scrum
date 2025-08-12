package com.ericxtchen.aiscrum.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teams")
public class Team {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(name = "team_name", nullable = false, unique = true)
    private String teamName;

    @Getter
    @Setter
    @OneToMany(
            mappedBy = "team",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Sprint> sprints = new ArrayList<>();

    @Getter
    @Setter
    @OneToMany(
            mappedBy = "team",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<MetricSnapshot> metricSnapshots = new ArrayList<>();

    @Getter
    @Setter
    @ManyToMany(mappedBy = "teams")
    private List<User> users = new ArrayList<>();
}

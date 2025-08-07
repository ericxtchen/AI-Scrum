package com.ericxtchen.aiscrum.entities;

import jakarta.persistence.*;

@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "team_name", nullable = false)
    private String teamName;

}

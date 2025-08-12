package com.ericxtchen.aiscrum.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(name = "principal_name", nullable = false, unique = true)
    private String principalName;

    @Getter
    @Setter
    @Column(nullable = false)
    private String email;

    @Getter
    @Setter
    private String displayName;

    @Getter
    @Setter
    @Column(length = 4096, nullable = false)
    private String accessToken;

    @Getter
    @Setter
    @Column(length = 4096, nullable = false)
    private String refreshToken;

    @Getter
    @Setter
    @Column(nullable = false)
    private Instant accessTokenExpiresAt;

    @Getter
    @Setter
    @Column(nullable = false)
    private String cloudId;

    @Getter
    @Setter
    @Column(nullable = false)
    private String siteUrl;

    @Getter
    @Setter
    @ManyToMany
    @JoinTable(
            name = "user_teams",
            joinColumns = @JoinColumn(name = "principal_name", referencedColumnName = "principalName"), // without referencedColumnName, it will default to the id
            inverseJoinColumns = @JoinColumn(name = "team_name", referencedColumnName = "teamName")
    )
    private List<Team> teams;
}

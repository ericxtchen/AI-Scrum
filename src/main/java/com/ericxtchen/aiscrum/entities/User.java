package com.ericxtchen.aiscrum.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Getter
    @Setter
    private String accessToken;

    @Getter
    @Setter
    private String refreshToken;
}

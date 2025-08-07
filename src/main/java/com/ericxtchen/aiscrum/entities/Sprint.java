package com.ericxtchen.aiscrum.entities;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
public class Sprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ElementCollection
    private List<String> tasks; // definitely needs a more complex data structure

    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date endDate;

}

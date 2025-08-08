package com.ericxtchen.aiscrum.controllers;

import com.ericxtchen.aiscrum.entities.Team;
import com.ericxtchen.aiscrum.repositories.TeamRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TestController {
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void init() throws SQLException {
        System.out.println("🔌 Connected to DB: " + dataSource.getConnection().getMetaData().getURL());
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping
    public Team createTicket(@RequestBody Team team){
        System.out.println("Received ticket: " + team);
        return teamRepository.save(team);
    }

    @GetMapping
    public List<Team> getAllTickets(){
        return teamRepository.findAll();
    }
}

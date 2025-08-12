package com.ericxtchen.aiscrum.controllers;

import com.ericxtchen.aiscrum.dto.JiraIssueResponse;
import com.ericxtchen.aiscrum.entities.Team;
import com.ericxtchen.aiscrum.entities.User;
import com.ericxtchen.aiscrum.repositories.SprintRepository;
import com.ericxtchen.aiscrum.repositories.TicketRepository;
import com.ericxtchen.aiscrum.repositories.UserRepository;
import com.ericxtchen.aiscrum.services.JiraIntegrationService;
import com.ericxtchen.aiscrum.dto.JiraTicketDto;
import com.ericxtchen.aiscrum.services.VelocityCalculatorService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/get/jira")
public class JiraController {
    private final JiraIntegrationService jiraIntegrationService;
    private final VelocityCalculatorService velocityCalculatorService;
    private final SprintRepository sprintRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    public JiraController(JiraIntegrationService jiraIntegrationService, VelocityCalculatorService velocityCalculatorService, SprintRepository sprintRepository, TicketRepository ticketRepository, UserRepository userRepository) {
        this.jiraIntegrationService = jiraIntegrationService;
        this.velocityCalculatorService = velocityCalculatorService;
        this.sprintRepository = sprintRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<?> getJira() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new RuntimeException("User is not authenticated");
            }
            OAuth2AuthenticationToken oauth2token = (OAuth2AuthenticationToken) authentication;
            OAuth2User oAuth2User = oauth2token.getPrincipal();
            String principalName = oAuth2User.getName();
            User user = userRepository.findByPrincipalName(principalName).orElseThrow(() -> new RuntimeException("User not found"));
            List<Team> teams = user.getTeams();


            //Mono<List<JiraTicketDto>> response = jiraIntegrationService.getClosedTicketsFromClosedSprint(authentication, 3); // should see all sprints

//            if (response != null) {
//                return ResponseEntity.ok(response);
//            } else {
//                // This case handles if the service call returns null for some reason.
//                return ResponseEntity.status(404).body("No issues found or response was null.");
//            }
        } catch (WebClientResponseException e) {
            // This is CRUCIAL: It catches API errors from Jira (like 401 Unauthorized, 404 Not Found).
            System.err.println("Error from Jira API: " + e.getResponseBodyAsString());
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body("Error fetching data from Jira: " + e.getResponseBodyAsString());
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        } catch (Exception e) {
            // This catches other unexpected errors.
            System.err.println("An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(500).body("An internal server error occurred.");
        }
    }
}

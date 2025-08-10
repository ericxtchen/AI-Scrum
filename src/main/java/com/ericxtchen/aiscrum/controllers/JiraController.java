package com.ericxtchen.aiscrum.controllers;

import com.ericxtchen.aiscrum.dto.JiraIssueResponse;
import com.ericxtchen.aiscrum.services.JiraIntegrationService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestController
@RequestMapping("/api/get/jira")
public class JiraController {
    private final JiraIntegrationService jiraIntegrationService;
    public JiraController(JiraIntegrationService jiraIntegrationService) {
        this.jiraIntegrationService = jiraIntegrationService;
    }

    @GetMapping
    public ResponseEntity<?> getJira() {
        try {
            JiraIssueResponse response = jiraIntegrationService.getClosedIssuesFromSprint().block();

            if (response != null) {
                return ResponseEntity.ok(response);
            } else {
                // This case handles if the service call returns null for some reason.
                return ResponseEntity.status(404).body("No issues found or response was null.");
            }
        } catch (WebClientResponseException e) {
            // This is CRUCIAL: It catches API errors from Jira (like 401 Unauthorized, 404 Not Found).
            System.err.println("Error from Jira API: " + e.getResponseBodyAsString());
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body("Error fetching data from Jira: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            // This catches other unexpected errors.
            System.err.println("An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(500).body("An internal server error occurred.");
        }
    }
}

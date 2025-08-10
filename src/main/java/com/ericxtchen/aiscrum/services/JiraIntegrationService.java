package com.ericxtchen.aiscrum.services;

import com.ericxtchen.aiscrum.dto.JiraIssueResponse;
import com.ericxtchen.aiscrum.dto.JiraSearchRequest;
import com.ericxtchen.aiscrum.entities.User;
import com.ericxtchen.aiscrum.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**
    A jira url can have multiple projects, teams, and groups.
    For searching sprints:
        The api endpoint is this: "https://api.atlassian.com/ex/jira/" +  cloudId + "/rest/api/3/search";
        and use JQL to search for sprints
        Code below is for finding closed issues from sprints
        Sprint name is the sprint id

    Goal is to find closed issues in a sprint for a team/group
    Flow is like this: get logged in user -> find what team/group they're in -> find which sprints they're in -> find closed issues within those sprints
    Something like that...
 */


@Service
public class JiraIntegrationService {

    private final UserRepository userRepository;
    private final WebClient webClient;

    public JiraIntegrationService(UserRepository userRepository, WebClient.Builder webClientBuilder) {
        this.userRepository = userRepository;
        this.webClient = webClientBuilder.build();
    }

    public JsonNode getTickets() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }
        String principalName = authentication.getName();
        User user = userRepository.findByPrincipalName(principalName).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String cloudId = user.getCloudId();
        String uri = "https://api.atlassian.com/ex/jira/" +  cloudId + "/rest/api/3/search";
        //String uri = "https://api.atlassian.com/ex/jira/" +  cloudId + "/rest/agile/1.0/board"; // Most likely not authenticating correctly
        TestObject testObject = new TestObject("sprint = 2 AND statusCategory = Done", new String[]{"summary", "status", "key"} , 50);
        JsonNode test = webClient.post()
                .uri(uri)
                .headers(headers -> headers.setBearerAuth(user.getAccessToken()))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        return test;
    }

    public Mono<JiraIssueResponse> getClosedIssuesFromSprint() {
        // 1. Construct the JQL query
        String jql = "sprint = SPRINT2 AND statusCategory = Done"; // SPRINT2 is the name of the sprint, not necessarily the sprint id?

        // 2. Create the request body object
        JiraSearchRequest requestBody = new JiraSearchRequest(
                jql,
                List.of("summary", "status", "key"),
                50
        );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }
        String principalName = authentication.getName();
        User user = userRepository.findByPrincipalName(principalName).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String cloudId = user.getCloudId();
        //String uri = "https://api.atlassian.com/ex/jira/" +  cloudId + "/rest/api/3/search";
        String uri = "https://api.atlassian.com/ex/jira/" +  cloudId + "/rest/api/3/user?accountId=712020:1c7e51b4-7e40-4c2e-be90-7284aed4ad40&expand=groups";

        // 3. Build and execute the API call
        return webClient.get()
                .uri(uri) // Populates the {cloudId} placeholder
                .headers(h -> h.setBearerAuth(user.getAccessToken())) // Sets the "Authorization: Bearer ..." header
                .accept(MediaType.APPLICATION_JSON)
                //.bodyValue(requestBody) // Sets the request body
                .retrieve() // Executes the request
                .bodyToMono(JiraIssueResponse.class); // Maps the JSON response to our DTO
    }
}

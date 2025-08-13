package com.ericxtchen.aiscrum.services;

import com.ericxtchen.aiscrum.dto.JiraTicketDto;
import com.ericxtchen.aiscrum.entities.User;
import com.ericxtchen.aiscrum.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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


record JiraSearchResponse(List<JiraTicketDto> issues) {}

@Service
public class JiraIntegrationService {

    private final UserRepository userRepository;
    private final WebClient webClient;

    public JiraIntegrationService(UserRepository userRepository, WebClient.Builder webClientBuilder, OAuth2AuthorizedClientManager authorizedClientManager, JiraFieldService jiraFieldService) {
        this.userRepository = userRepository;
        this.webClient = webClientBuilder.build();
    }

    public List<JiraTicketDto> getClosedTicketsFromSprint(OAuth2AuthorizedClient authorizedClient, Long sprintId, String storyPointsId) {
        if (authorizedClient == null) {
            throw new UsernameNotFoundException(authorizedClient.getPrincipalName());
        }

        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();

        User user = userRepository.findByPrincipalName(authorizedClient.getPrincipalName()).orElseThrow(() -> new UsernameNotFoundException(authorizedClient.getPrincipalName()));
        String cloudId = user.getCloudId();

        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.atlassian.com")
                        .path("/ex/jira/{cloudId}/rest/api/3/search")
                        .queryParam("jql", String.format("sprint = %d AND statusCategory = 'Done'", sprintId))
                        .queryParam("fields", "summary,status," + storyPointsId)
                        .build(cloudId))
                .headers(headers -> headers.setBearerAuth(accessToken.getTokenValue()))
                .retrieve()
                .bodyToMono(JiraSearchResponse.class)
                .map(JiraSearchResponse::issues)
                .block();
    }
}

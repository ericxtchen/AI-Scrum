package com.ericxtchen.aiscrum.services;

import com.ericxtchen.aiscrum.entities.User;
import com.ericxtchen.aiscrum.repositories.UserRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class JiraFieldService {
    public final WebClient webClient;
    public final UserRepository userRepository;
    private String storyPointsFieldId;

    public JiraFieldService(WebClient.Builder webClient, UserRepository userRepository) {
        this.webClient = webClient.build();
        this.userRepository = userRepository;
    }

    public String getStoryPointsFieldId(OAuth2AuthorizedClient authorizedClient) {
        if (storyPointsFieldId != null) {
            return storyPointsFieldId;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByPrincipalName(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException(authentication.getName()));

        String cloudId = user.getCloudId();
        String token = authorizedClient.getAccessToken().getTokenValue();

        List<Map<String, Object>> fields = this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.atlassian.com")
                        .path("/ex/jira/{cloudId}/rest/api/3/field")
                        .build(cloudId))
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {
                })
                .block();

        if (fields != null) {
            storyPointsFieldId = fields.stream()
                    .filter(f -> {
                        String name = ((String) f.get("name")).toLowerCase();
                        return name.equals("story points") || name.equals("story point estimate");
                    })
                    .map(f -> (String) f.get("id"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Story Points field not found in Jira"));
        }

        return storyPointsFieldId;
    }
}

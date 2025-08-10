package com.ericxtchen.aiscrum.config;

import com.ericxtchen.aiscrum.entities.User;
import com.ericxtchen.aiscrum.repositories.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;

@Component
public class CustomJiraOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private static final Logger LOGGER = Logger.getLogger(CustomJiraOAuth2SuccessHandler.class.getName());

    private final UserRepository userRepository;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    private final WebClient webClient;

    public CustomJiraOAuth2SuccessHandler(OAuth2AuthorizedClientService oAuth2AuthorizedClientService,
                                           UserRepository userRepository,
                                           WebClient.Builder webClientBuilder) {
        this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
        this.userRepository = userRepository;
        this.webClient = webClientBuilder.build();
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AuthenticationToken oauth2token =  (OAuth2AuthenticationToken) authentication;

        OAuth2User oAuth2User = oauth2token.getPrincipal();
        String principalName = oAuth2User.getName();
        String email =  oAuth2User.getAttribute("email");
        String displayName = oAuth2User.getAttribute("name");

        OAuth2AuthorizedClient client = oAuth2AuthorizedClientService.loadAuthorizedClient(oauth2token.getAuthorizedClientRegistrationId(), principalName);
        if (client == null) {
            LOGGER.log(Level.SEVERE, "Could not load authorized client for principal: " + principalName);
            response.sendRedirect("/error?message=client_not_found");
            return;
        }
        String accessToken = client.getAccessToken().getTokenValue();
        String refreshToken = client.getRefreshToken().getTokenValue();
        long accessTokenExpiresAt = client.getAccessToken().getExpiresAt().getEpochSecond();

        String cloudId = null;
        String siteUrl = null;

        try {
            JsonNode[] accessibleResources = webClient.get() // WebClient is like fetch in js
                    .uri("https://api.atlassian.com/oauth/token/accessible-resources")
                    .headers(headers -> headers.setBearerAuth(accessToken))
                    .retrieve()
                    .bodyToMono(JsonNode[].class)
                    .block();

            if (accessibleResources != null && accessibleResources.length > 0) {
                JsonNode jiraSite = accessibleResources[0]; // getting the first item is just for now, should create a way for the user to choose
                cloudId = jiraSite.get("id").asText();
                siteUrl = jiraSite.get("url").asText();
            }
        } catch (WebClientResponseException e) {
            LOGGER.log(Level.SEVERE, "Error fetching accessible resources from Atlassian API: " + e.getResponseBodyAsString(), e);
            response.sendRedirect("/error?message=api_error");
            return;
        }

        if (cloudId == null) {
            LOGGER.log(Level.WARNING, "No accessible Jira sites found for principal: " + principalName);
            response.sendRedirect("/error?message=no_site_access");
            return;
        }

        User user = userRepository.findByPrincipalName(principalName)
                .orElse(new User());

        user.setPrincipalName(principalName);
        user.setEmail(email);
        user.setDisplayName(displayName);
        user.setAccessToken(accessToken);
        user.setRefreshToken(refreshToken);
        user.setAccessTokenExpiresAt(java.time.Instant.ofEpochSecond(accessTokenExpiresAt));
        user.setCloudId(cloudId);
        user.setSiteUrl(siteUrl);
        userRepository.save(user);
        LOGGER.log(Level.INFO, "Successfully saved user data for principal: " + principalName);

        try {
            response.sendRedirect("https://guppy-shining-surely.ngrok-free.app/api/get/jira");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

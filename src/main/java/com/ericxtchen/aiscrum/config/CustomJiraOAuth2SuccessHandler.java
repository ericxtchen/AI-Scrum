package com.ericxtchen.aiscrum.config;

import com.ericxtchen.aiscrum.entities.User;
import com.ericxtchen.aiscrum.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomJiraOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        OAuth2AuthenticationToken oauth2token =  (OAuth2AuthenticationToken) authentication;

        OAuth2User oAuth2User = oauth2token.getPrincipal();
        String principalName = oAuth2User.getName();
        String email =  oAuth2User.getAttribute("email");
        String displayName = oAuth2User.getAttribute("name");

        OAuth2AuthorizedClient client = oAuth2AuthorizedClientService.loadAuthorizedClient(oauth2token.getAuthorizedClientRegistrationId(), principalName);
        String accessToken = client.getAccessToken().getTokenValue();
        String refreshToken = client.getRefreshToken().getTokenValue();
        long accessTokenExpiresAt = client.getAccessToken().getExpiresAt().getEpochSecond();

        User user = userRepository.findByPrincipalName(principalName)
                .orElse(new User());

        user.setPrincipalName(principalName);
        user.setEmail(email);
        user.setDisplayName(displayName);
        user.setAccessToken(accessToken);
        user.setRefreshToken(refreshToken);
        user.setAccessTokenExpiresAt(java.time.Instant.ofEpochSecond(accessTokenExpiresAt));
        userRepository.save(user);

        try {
            response.sendRedirect("http://localhost:3000/dashboard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

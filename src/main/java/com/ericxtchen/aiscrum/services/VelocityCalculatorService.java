package com.ericxtchen.aiscrum.services;

import com.ericxtchen.aiscrum.entities.Sprint;
import com.ericxtchen.aiscrum.entities.Team;
import com.ericxtchen.aiscrum.entities.User;
import com.ericxtchen.aiscrum.repositories.SprintRepository;
import com.ericxtchen.aiscrum.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VelocityCalculatorService {
    private final SprintRepository sprintRepository;
    private final UserRepository userRepository;
    private final JiraIntegrationService jiraIntegrationService;
    public VelocityCalculatorService(SprintRepository sprintRepository, UserRepository userRepository, JiraIntegrationService jiraIntegrationService) {
        this.sprintRepository = sprintRepository;
        this.userRepository = userRepository;
        this.jiraIntegrationService =  jiraIntegrationService;
    }

    public void calculateAverageVelocity(Authentication authentication, Long teamId) {
        // Use the teamId to find the latest 10 sprints associated with that team
        // find the team the user is in
        // find which sprints the team is in

        // Delegate below 5 lines of commented code to the controller
        OAuth2AuthenticationToken oauth2token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oauth2token.getPrincipal();
        String principalName = oAuth2User.getName();
        User user = userRepository.findByPrincipalName(principalName).orElseThrow(() -> new RuntimeException("User not found"));
        List<Team> teams = user.getTeams();
        Map<Long, List<Sprint>> latestSprintsByTeam = new HashMap<>();
        for (Team team : teams) {
            List<Sprint> sprints = sprintRepository.findTop10ByTeamIdOrderByStartDateDesc(team.getId());
            latestSprintsByTeam.put(team.getId(), sprints);
        }

    }
}


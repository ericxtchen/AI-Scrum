package com.ericxtchen.aiscrum.services;

import com.ericxtchen.aiscrum.dto.JiraTicketDto;
import com.ericxtchen.aiscrum.entities.MetricSnapshot;
import com.ericxtchen.aiscrum.entities.Sprint;
import com.ericxtchen.aiscrum.entities.Team;
import com.ericxtchen.aiscrum.repositories.MetricSnapshotRepository;
import com.ericxtchen.aiscrum.repositories.SprintRepository;
import com.ericxtchen.aiscrum.repositories.TeamRepository;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;

@Service
public class VelocityCalculatorService {
    private final SprintRepository sprintRepository;
    private final TeamRepository teamRepository;
    private final MetricSnapshotRepository metricSnapshotRepository;
    private final JiraIntegrationService jiraIntegrationService;
    public VelocityCalculatorService(SprintRepository sprintRepository, TeamRepository teamRepository, MetricSnapshotRepository metricSnapshotRepository, JiraIntegrationService jiraIntegrationService,  JiraFieldService jiraFieldService) {
        this.sprintRepository = sprintRepository;
        this.teamRepository = teamRepository;
        this.metricSnapshotRepository = metricSnapshotRepository;
        this.jiraIntegrationService =  jiraIntegrationService;
    }

    public void calculateAverageVelocity(OAuth2AuthorizedClient authorizedClient, Long teamId, String storyPointsId) {
        // Use the teamId to find the latest 10 sprints associated with that team
        // find the team the user is in
        // find which sprints the team is in

        // Delegate below 5 lines of commented code to the controller
//        OAuth2AuthenticationToken oauth2token = (OAuth2AuthenticationToken) authentication;
//        OAuth2User oAuth2User = oauth2token.getPrincipal();
//        String principalName = oAuth2User.getName();
//        User user = userRepository.findByPrincipalName(principalName).orElseThrow(() -> new RuntimeException("User not found"));
//        List<Team> teams = user.getTeams();
        // for each sprint, get the tickets and its story points
        List<Sprint> sprints = sprintRepository.findTop10ByTeamIdOrderByStartDateDesc(teamId);
//        List<JiraTicketDto> tickets = null;
//        sprints.forEach(sprint -> {tickets.add(jiraIntegrationService.getClosedTicketsFromSprint(authentication, sprint.getId()))});
        Map<Long, List<JiraTicketDto>> jiraTicketsBySprintId = new HashMap<>();
        sprints.forEach(sprint -> {
            jiraTicketsBySprintId.put(sprint.getId(), jiraIntegrationService.getClosedTicketsFromSprint(authorizedClient, sprint.getId(), storyPointsId));
        });
        Map<Long, Double> sumOfStoryPointsBySprint = new HashMap<>();
        jiraTicketsBySprintId.forEach((sprintId, tickets) -> {sumOfStoryPointsBySprint.put(sprintId, tickets.stream()
                .map(ticket -> ticket.fields().get(storyPointsId)) // get value from map
                .filter(Objects::nonNull) // avoid NPE
                .mapToDouble(val -> ((Number) val).doubleValue())  // convert to double
                .average()
                .orElse(0.0));
        });

        sumOfStoryPointsBySprint.forEach((sprintId, average) -> {
            Team team = teamRepository.findById(teamId).orElse(null);
            Sprint sprint = sprintRepository.findById(sprintId).orElse(null);
            MetricSnapshot metricSnapshot = new MetricSnapshot();
            metricSnapshot.setTeam(team);
            metricSnapshot.setMetricValue(average);
            metricSnapshot.setSprint(sprintRepository.findById(sprintId).get());
            metricSnapshot.setMetricName(team.getTeamName() + " " + sprint.getSprintName() + " " + "Average Velocity");
            metricSnapshot.setTimestamp(OffsetDateTime.now());
            metricSnapshotRepository.save(metricSnapshot);
        });
    }
}


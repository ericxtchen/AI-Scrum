package com.ericxtchen.aiscrum.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

// Represents the JSON body we will send to Jira
public record JiraSearchRequest(String jql, List<String> fields, int maxResults) {
}


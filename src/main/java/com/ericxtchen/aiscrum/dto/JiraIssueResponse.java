package com.ericxtchen.aiscrum.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record JiraIssueResponse(List<Issue> issues, int total) {
}

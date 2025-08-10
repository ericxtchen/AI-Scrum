package com.ericxtchen.aiscrum.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record IssueFields(String summary, Status status) {
}

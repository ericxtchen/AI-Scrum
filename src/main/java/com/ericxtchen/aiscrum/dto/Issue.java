package com.ericxtchen.aiscrum.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Issue(String key, IssueFields fields) {
}
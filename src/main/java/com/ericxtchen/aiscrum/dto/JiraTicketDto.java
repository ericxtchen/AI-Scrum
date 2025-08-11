package com.ericxtchen.aiscrum.dto;

import java.util.Map;


public record JiraTicketDto(String key, Map<String, Object> fields) {}


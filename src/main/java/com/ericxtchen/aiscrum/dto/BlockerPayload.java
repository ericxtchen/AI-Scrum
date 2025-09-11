package com.ericxtchen.aiscrum.dto;

import java.util.List;

public record BlockerPayload(String description, List<String> involvedPeople) {}

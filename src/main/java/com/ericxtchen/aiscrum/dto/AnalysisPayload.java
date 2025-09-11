package com.ericxtchen.aiscrum.dto;

import java.util.List;

// This record represents the JSON payload sent by your Python microservice.
public record AnalysisPayload(
        String meetingId, // A unique ID for the meeting from Recall.ai
        Long sprintId, // Get this from the user...
        String meetingDate,
        String summary,
        List<BlockerPayload> blockers
) {}



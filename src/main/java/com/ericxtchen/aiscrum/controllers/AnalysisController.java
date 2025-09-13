package com.ericxtchen.aiscrum.controllers;

import com.ericxtchen.aiscrum.entities.MeetingSummary;
import com.ericxtchen.aiscrum.repositories.BlockerRepository;
import com.ericxtchen.aiscrum.repositories.MeetingSummaryRepository;
import com.ericxtchen.aiscrum.services.ReceiveProcessedTranscript;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {
    public final MeetingSummaryRepository meetingSummaryRepository;
    public final BlockerRepository blockerRepository;

    public AnalysisController(MeetingSummaryRepository meetingSummaryRepository, BlockerRepository blockerRepository) {
        this.meetingSummaryRepository = meetingSummaryRepository;
        this.blockerRepository = blockerRepository;
    }

    @GetMapping("/teams/{sprintId}/summary")
    public ResponseEntity<String> getMeetingSummary(@PathVariable("sprintId") Long sprintId) {
        LocalDate now =  LocalDate.now(); // LocalDate for dev purposes only. Doesn't handle timezones
        return meetingSummaryRepository
                .findBySprint_IdAndMeetingDate(sprintId, now)
                .map(summary -> ResponseEntity.ok(summary.getSummaryText()))
                .orElse(ResponseEntity.notFound().build());
    }
}

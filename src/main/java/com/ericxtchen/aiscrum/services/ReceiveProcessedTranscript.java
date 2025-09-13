package com.ericxtchen.aiscrum.services;

import com.ericxtchen.aiscrum.dto.AnalysisPayload;
import com.ericxtchen.aiscrum.entities.Blocker;
import com.ericxtchen.aiscrum.entities.MeetingSummary;
import com.ericxtchen.aiscrum.entities.Sprint;
import com.ericxtchen.aiscrum.repositories.MeetingSummaryRepository;
import com.ericxtchen.aiscrum.repositories.SprintRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ReceiveProcessedTranscript {
    public final MeetingSummaryRepository meetingSummaryRepository;
    public final SprintRepository sprintRepository;
    public ReceiveProcessedTranscript(MeetingSummaryRepository meetingSummaryRepository, SprintRepository sprintRepository) {
        this.meetingSummaryRepository = meetingSummaryRepository;
        this.sprintRepository = sprintRepository;
    }

    @KafkaListener(topics = "processed-transcripts")
    public void listen(AnalysisPayload analysis) {
        // 1. Find the Sprint this analysis belongs to
        Sprint sprint = sprintRepository.findById(analysis.sprintId())
                .orElseThrow(() -> new EntityNotFoundException("Sprint not found"));

        // 2. Create the main summary entity
        MeetingSummary summary = new MeetingSummary();
        summary.setSprint(sprint);
        summary.setSummaryText(analysis.summary());
        summary.setMeetingDate(LocalDate.parse(analysis.meetingDate()));

        // 3. Create Blocker entities from the analysis
        analysis.blockers().forEach(desc -> {
            Blocker blocker = new Blocker();
            blocker.setDescription(desc.description());
            blocker.setMeetingSummary(summary);
            summary.getBlockers().add(blocker);
        });

        // 4. Save everything to the database in one transaction
        meetingSummaryRepository.save(summary);
    }
}

package com.ericxtchen.aiscrum.repositories;

import com.ericxtchen.aiscrum.entities.MeetingSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface MeetingSummaryRepository extends JpaRepository<MeetingSummary, Long> {
    Optional<MeetingSummary> findBySprint_IdAndMeetingDate(Long sprintId, LocalDate meetingDate);
}

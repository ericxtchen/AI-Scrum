package com.ericxtchen.aiscrum.repositories;

import com.ericxtchen.aiscrum.entities.MeetingSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingSummaryRepository extends JpaRepository<MeetingSummary, Long> {

}

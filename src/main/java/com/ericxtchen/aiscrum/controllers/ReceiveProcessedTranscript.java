package com.ericxtchen.aiscrum.controllers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ReceiveProcessedTranscript {
    @KafkaListener(topics = "processed-transcripts")
    public void listen(String msg) {
        System.out.println("RECEIVED PROCESSED TRANSCRIPT: " +  msg);
    }
}

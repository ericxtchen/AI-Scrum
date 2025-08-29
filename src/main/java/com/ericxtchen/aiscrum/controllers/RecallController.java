package com.ericxtchen.aiscrum.controllers;

import com.ericxtchen.aiscrum.dto.RecallAIMeeting;
import com.ericxtchen.aiscrum.dto.recall_webhook.RecallWebhookPayload;
import com.ericxtchen.aiscrum.services.RecallBotService;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/recall")
public class RecallController {
    private final KafkaTemplate<String, String> kafkaTemplate;
    public final RecallBotService recallBotService;
    public RecallController(KafkaTemplate<String, String> kafkaTemplate, RecallBotService recallBotService) {
        this.kafkaTemplate = kafkaTemplate;
        this.recallBotService = recallBotService;
    }

    @GetMapping("/meeting")
    public Mono<String> getMeeting(@RequestBody RecallAIMeeting meeting){
        return recallBotService.enterMeeting(meeting);
    }
    @PostMapping("/webhook")
    public ResponseEntity<Void> recallWebhook(@RequestBody RecallWebhookPayload recallWebhookPayload) {
        if ("transcript.done".equals(recallWebhookPayload.getEvent())) {
            String botId = recallWebhookPayload.getData().getBot().getId(); // bot_id is not being received for some reason...
            System.out.println("BOT ID: " + botId);
            this.kafkaTemplate.send("transcripts-to-process", botId);
        }
        return ResponseEntity.ok().build();
    }
}

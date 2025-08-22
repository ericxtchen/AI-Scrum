package com.ericxtchen.aiscrum.controllers;

import com.ericxtchen.aiscrum.dto.RecallAIMeeting;
import com.ericxtchen.aiscrum.dto.recall_webhook.RecallWebhookPayload;
import com.ericxtchen.aiscrum.services.RecallBotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/recall")
public class RecallController {
    public final RecallBotService recallBotService;
    public RecallController(RecallBotService recallBotService) {
        this.recallBotService = recallBotService;
    }

    @GetMapping("meeting")
    public Mono<String> getMeeting(@RequestBody RecallAIMeeting meeting){
        return recallBotService.enterMeeting(meeting);
    }
    @PostMapping("/webhook")
    public ResponseEntity<Void> recallWebhook(@RequestBody RecallWebhookPayload recallWebhookPayload) {
        if ("transcript.done".equals(recallWebhookPayload.getEvent())) {
            String botId = recallWebhookPayload.getData().getBot().getBotId();
            // send botId to Kafka
        }
        return ResponseEntity.ok().build();
    }
}

package com.ericxtchen.aiscrum.services;

import com.ericxtchen.aiscrum.dto.RecallAIMeeting;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
public class RecallBotService {
    public final WebClient webClient;

    public RecallBotService(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }

    public Mono<String> enterMeeting(RecallAIMeeting  recallAIMeeting) {
        return webClient.post()
                .uri("https://us-west-2.recall.ai/api/v1/bot/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + System.getenv("RECALL_API_KEY"))
                .bodyValue(recallAIMeeting) // the meeting URL
                .exchangeToMono(response -> {
                    int statusCode = response.statusCode().value();
                    if (statusCode >= 200 && statusCode < 300) {
                        return response.bodyToMono(String.class);
                    } else {
                        return Mono.error(new RuntimeException("HTTP error: " + response.statusCode()));
                    }
                })
                .retryWhen(
                        Retry.fixedDelay(3, Duration.ofSeconds(2))
                );
    }
}

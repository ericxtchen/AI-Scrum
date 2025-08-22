package com.ericxtchen.aiscrum.dto.recall_webhook;

import lombok.Getter;
import lombok.Setter;

public class RecallWebhookPayload {
    @Getter
    @Setter
    public String event;

    @Getter
    @Setter
    public Data data;
}

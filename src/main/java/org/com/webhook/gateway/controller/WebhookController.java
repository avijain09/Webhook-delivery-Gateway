package org.com.webhook.gateway.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.com.webhook.gateway.service.WebhookEventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhooks")
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookEventService webhookEventService;

    @PostMapping("/ingest")
    public ResponseEntity<String> ingestWebhook(
            @RequestParam String tenantId,
            @RequestParam String eventType,
            @RequestParam String eventId,
            @RequestBody String payload) throws JsonProcessingException {

        webhookEventService.saveEvent(tenantId, eventType, eventId, payload);
        return ResponseEntity.ok("Webhook received");
    }
}
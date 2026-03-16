package org.com.webhook.gateway.service;

import org.com.webhook.gateway.entity.WebhookEvent;
import org.com.webhook.gateway.repository.WebhookEventRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static org.com.webhook.gateway.model.WebhookStatus.DELIVERED;
import static org.com.webhook.gateway.model.WebhookStatus.FAILED;

@Service
public class KafkaConsumerService {

    private final WebhookEventRepository repository;

    public KafkaConsumerService(WebhookEventRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "webhook-events", groupId = "webhook-group")
    public void consume(String eventId) {

        WebhookEvent event = repository.findByEventId(eventId);

        try {
            // simulate processing
            System.out.println("Processing event: " + eventId);

            event.setStatus(DELIVERED);

        } catch (Exception e) {
            event.setStatus(FAILED);
        }

        repository.save(event);
    }
}

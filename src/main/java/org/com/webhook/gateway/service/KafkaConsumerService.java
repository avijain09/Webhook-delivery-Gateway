package org.com.webhook.gateway.service;

import lombok.RequiredArgsConstructor;
import org.com.webhook.gateway.entity.WebhookEvent;
import org.com.webhook.gateway.repository.WebhookEventRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static org.com.webhook.gateway.model.WebhookStatus.DELIVERED;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final WebhookDeliveryService deliveryService;
    private final WebhookEventRepository repository;

    @KafkaListener(topics = "webhook-events", groupId = "webhook-group")
    public void consumeMain(String eventId) {
        System.out.println("Consuming as Main");
        WebhookEvent event = repository.findByEventId(eventId);
        deliveryService.deliver(event);
    }

    @KafkaListener(topics = "webhook-retry-1", groupId = "webhook-group")
    public void retry1(String eventId) {
        WebhookEvent event=repository.findByEventId(eventId);
        if (event.getStatus() == DELIVERED) return;
        deliveryService.deliver(event);
    }

    @KafkaListener(topics = "webhook-retry-2", groupId = "webhook-group")
    public void retry2(String eventId) {
        WebhookEvent event=repository.findByEventId(eventId);
        if (event.getStatus() == DELIVERED) return;
        deliveryService.deliver(repository.findByEventId(eventId));
    }

    @KafkaListener(topics = "webhook-retry-3", groupId = "webhook-group")
    public void retry3(String eventId) {
        WebhookEvent event=repository.findByEventId(eventId);
        if (event.getStatus() == DELIVERED) return;
        deliveryService.deliver(repository.findByEventId(eventId));
    }

    @KafkaListener(topics = "webhook-dlq")
    public void consumeDLQ(String eventId) {
        System.out.println("🚨 FINAL FAILURE: " + eventId);
    }
}
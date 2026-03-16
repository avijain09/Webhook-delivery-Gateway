package org.com.webhook.gateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.com.webhook.gateway.entity.WebhookEvent;
import org.com.webhook.gateway.repository.WebhookEventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.com.webhook.gateway.model.WebhookStatus.RECEIVED;

@Service
@RequiredArgsConstructor
public class WebhookEventService {

    private final WebhookEventRepository repository;
    private final WebhookProcessorService processorService;
    private final KafkaProducerService kafkaProducerService;
    public WebhookEvent saveEvent(String tenantId,
                                  String eventType,
                                  String eventId,
                                  String payload) throws JsonProcessingException {
        Optional<WebhookEvent> existing =
                repository.findByTenantIdAndEventId(tenantId, eventId);

        if (existing.isPresent()) {
            return existing.get();   // Idempotent behaviour
        }

        WebhookEvent event = WebhookEvent.builder()
                .tenantId(tenantId)
                .eventId(eventId)   // idempotency key
                .eventType(eventType)
                .payload(payload)
                .status(RECEIVED)
                .retryCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        WebhookEvent saved = repository.save(event);

        kafkaProducerService.publishEvent(event.getEventId());
        processorService.processEvent(saved);
        return saved;

    }
}
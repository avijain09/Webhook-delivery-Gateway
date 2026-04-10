package org.com.webhook.gateway.service;

import lombok.RequiredArgsConstructor;
import org.com.webhook.gateway.entity.WebhookEvent;
import org.com.webhook.gateway.repository.WebhookEventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.com.webhook.gateway.model.WebhookStatus.RECEIVED;

@Service
@RequiredArgsConstructor
public class WebhookEventService {

    private final WebhookEventRepository repository;
    private final KafkaProducerService kafkaProducerService;

    public WebhookEvent saveEvent(String tenantId,
                                  String eventType,
                                  String eventId,
                                  String payload) {

        Optional<WebhookEvent> existing =
                repository.findByTenantIdAndEventId(tenantId, eventId);

        if (existing.isPresent()) {
            return existing.get();
        }

        WebhookEvent event = WebhookEvent.builder()
                .tenantId(tenantId)
                .eventId(eventId)
                .eventType(eventType)
                .payload(payload)
                .status(RECEIVED)
                .retryCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .version("v1")
                .build();

        WebhookEvent saved = repository.save(event);

        kafkaProducerService.sendToMain(saved.getEventId());

        return saved;
    }
}
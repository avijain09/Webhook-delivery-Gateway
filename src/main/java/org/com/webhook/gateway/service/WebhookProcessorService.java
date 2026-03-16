package org.com.webhook.gateway.service;

import lombok.RequiredArgsConstructor;
import org.com.webhook.gateway.entity.WebhookEvent;
import org.com.webhook.gateway.model.WebhookStatus;
import org.com.webhook.gateway.repository.WebhookEventRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WebhookProcessorService {

    private final WebhookEventRepository repository;

    @Async
    public void processEvent(WebhookEvent event) {

        event.setStatus(WebhookStatus.PROCESSING);
        repository.save(event);

        try {
            // Simulate external call
            Thread.sleep(2000);

            event.setStatus(WebhookStatus.DELIVERED);

        } catch (Exception e) {
            event.setStatus(WebhookStatus.FAILED);
            event.setRetryCount(event.getRetryCount() + 1);
        }

        event.setUpdatedAt(LocalDateTime.now());
        repository.save(event);
    }
}
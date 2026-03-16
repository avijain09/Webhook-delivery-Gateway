package org.com.webhook.gateway.repository;

import org.com.webhook.gateway.entity.WebhookEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WebhookEventRepository extends JpaRepository<WebhookEvent, Long> {
    Optional<WebhookEvent> findByTenantIdAndEventId(String tenantId, String eventId);

    WebhookEvent findByEventId(String eventId);
}
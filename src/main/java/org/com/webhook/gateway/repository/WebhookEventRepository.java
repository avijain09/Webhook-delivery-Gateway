package org.com.webhook.gateway.repository;

import org.com.webhook.gateway.entity.WebhookEvent;
import org.com.webhook.gateway.model.WebhookStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface WebhookEventRepository extends JpaRepository<WebhookEvent, Long> {

    Optional<WebhookEvent> findByTenantIdAndEventId(String tenantId, String eventId);

    WebhookEvent findByEventId(String eventId);

    @Modifying
    @Transactional
    @Query("UPDATE WebhookEvent e SET e.status = :status WHERE e.eventId = :eventId")
    void updateStatusByEventId(@Param("eventId") String eventId,
                               @Param("status") WebhookStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE WebhookEvent e SET e.retryCount = e.retryCount + 1 WHERE e.eventId = :eventId")
    void incrementRetry(@Param("eventId") String eventId);

    @Query("SELECT e.retryCount FROM WebhookEvent e WHERE e.eventId = :eventId")
    int getRetryCount(@Param("eventId") String eventId);

    @Modifying
    @Transactional
    @Query("UPDATE WebhookEvent e SET e.updatedAt = :time WHERE e.eventId = :eventId")
    void updateUpdatedAt(@Param("eventId") String eventId,
                         @Param("time") LocalDateTime time);
}
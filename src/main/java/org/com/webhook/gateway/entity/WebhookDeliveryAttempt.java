package org.com.webhook.gateway.entity;

import java.time.LocalDateTime;

import org.com.webhook.gateway.model.WebhookStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "webhook_delivery_attempt")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebhookDeliveryAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String webhookEventId;
    private String subscriptionId;
    private Integer attemptNumber;
    @Column(columnDefinition = "TEXT")
    private String requestHeaders;
    private Integer responseStatusCode;
    @Column(columnDefinition = "TEXT")
    private String responseBody;
    private LocalDateTime createdAt;
    private WebhookStatus status;
}

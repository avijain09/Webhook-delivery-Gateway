package org.com.webhook.gateway.entity;

import jakarta.persistence.*;
import lombok.*;
import org.com.webhook.gateway.model.WebhookStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "webhook_events", uniqueConstraints = {
                @UniqueConstraint(columnNames = { "tenantId", "eventId" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebhookEvent {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String tenantId;

        @Column(name = "event_id", unique = true)
        private String eventId;

        private String eventType;

        @Column(columnDefinition = "TEXT")
        private String payload;

        private Integer retryCount = 0;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        @Enumerated(EnumType.STRING)
        private WebhookStatus status;

        private String version;
}
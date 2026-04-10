package org.com.webhook.gateway.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tenant_config")
public class TenantConfig {

    @Id
    private String tenantId;

    private String webhookUrl;

    private String secret;
}
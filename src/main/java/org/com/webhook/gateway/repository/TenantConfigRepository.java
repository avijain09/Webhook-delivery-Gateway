package org.com.webhook.gateway.repository;

import org.com.webhook.gateway.entity.TenantConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantConfigRepository extends JpaRepository<TenantConfig, String> {

}
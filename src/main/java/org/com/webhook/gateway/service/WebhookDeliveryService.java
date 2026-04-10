package org.com.webhook.gateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.webhook.gateway.entity.TenantConfig;
import org.com.webhook.gateway.entity.WebhookEvent;
import org.com.webhook.gateway.model.WebhookStatus;
import org.com.webhook.gateway.repository.TenantConfigRepository;
import org.com.webhook.gateway.repository.WebhookEventRepository;
import org.com.webhook.gateway.utils.HmacUtil;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebhookDeliveryService {

    private final WebhookEventRepository repository;
    private final TenantConfigRepository tenantRepository;
    private final KafkaProducerService kafkaProducerService;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final int MAX_RETRIES = 3;

    public void deliver(WebhookEvent event) {

        log.info("Delivering event {}", event.getEventId());

        // ✅ Idempotency check
        if (event.getStatus() == WebhookStatus.DELIVERED) {
            log.info("Already delivered: {}", event.getEventId());
            return;
        }

        try {
            // ✅ Fetch tenant config
            TenantConfig tenant = tenantRepository
                    .findById(event.getTenantId())
                    .orElseThrow(() -> new RuntimeException("Tenant not found"));

            String url = tenant.getWebhookUrl();
            System.out.println("URL: "+url);
            // ✅ Build payload with version
            String payload = "{ \"version\": \"" + event.getVersion() + "\", \"data\": " + event.getPayload() + "}";
            System.out.println("Payload: "+payload);
            // ✅ HMAC Signature
            String signature = HmacUtil.generateSignature(payload, tenant.getSecret());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("X-Signature", signature);

            HttpEntity<String> request = new HttpEntity<>(payload, headers);

            // 🚀 Actual API call
            restTemplate.postForObject(url, request, String.class);
            System.out.println("API call made");
            // ✅ Success
            repository.updateStatusByEventId(event.getEventId(), WebhookStatus.DELIVERED);
            repository.updateUpdatedAt(event.getEventId(), LocalDateTime.now());

        } catch (Exception e) {

            repository.incrementRetry(event.getEventId());
            int retryCount = repository.getRetryCount(event.getEventId());

            if (retryCount < MAX_RETRIES) {

                repository.updateStatusByEventId(event.getEventId(), WebhookStatus.RETRYING);
                kafkaProducerService.sendToRetry(event.getEventId(), retryCount);

            } else {

                repository.updateStatusByEventId(event.getEventId(), WebhookStatus.FAILED);
                kafkaProducerService.sendToDLQ(event.getEventId());
            }
        }
    }
}
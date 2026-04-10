package org.com.webhook.gateway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendToMain(String eventId) {
        kafkaTemplate.send("webhook-events", eventId)
                .whenComplete((res, ex) -> {
                    if (ex == null) {
                        System.out.println("Sent to Kafka: " + eventId);
                    } else {
                        System.out.println("Failed to send: " + ex.getMessage());
                    }
                });
    }

    public void sendToRetry(String eventId, int retryCount) {

        if (retryCount == 1) {
            kafkaTemplate.send("webhook-retry-1", eventId);
        } else if (retryCount == 2) {
            kafkaTemplate.send("webhook-retry-2", eventId);
        } else if (retryCount == 3) {
            kafkaTemplate.send("webhook-retry-3", eventId);
        }
    }
    public void sendToDLQ(String eventId) {
        kafkaTemplate.send("webhook-dlq", eventId);
    }
}
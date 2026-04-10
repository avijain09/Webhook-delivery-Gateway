package org.com.webhook.gateway.model;

public enum WebhookStatus {
    RECEIVED,
    PROCESSING,
    DELIVERED,
    FAILED,
    RETRYING
}
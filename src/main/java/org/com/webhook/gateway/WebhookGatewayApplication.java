package org.com.webhook.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableKafka
@EnableAsync
@SpringBootApplication
public class WebhookGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebhookGatewayApplication.class, args);
    }
}
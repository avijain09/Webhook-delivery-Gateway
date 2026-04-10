package org.com.webhook.gateway.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestWebhookController {

    @PostMapping("/webhook")
    public String test(@RequestBody String payload,
                       @RequestHeader("X-Signature") String signature) {

        System.out.println("Received payload: " + payload);
        System.out.println("Signature: " + signature);

        return "OK";
    }
}

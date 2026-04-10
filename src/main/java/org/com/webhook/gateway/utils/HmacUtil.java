package org.com.webhook.gateway.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class HmacUtil {

    public static String generateSignature(String payload, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            mac.init(keySpec);

            byte[] rawHmac = mac.doFinal(payload.getBytes());
            return Base64.getEncoder().encodeToString(rawHmac);

        } catch (Exception e) {
            throw new RuntimeException("Error generating HMAC", e);
        }
    }
}
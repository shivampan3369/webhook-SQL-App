package com.example.bfhl.dto;

import lombok.Data;

@Data
public class GenerateWebhookResponse {
    private String webhook;      // returned URL to submit to (FYI, not strictly needed if using fixed /testWebhook/JAVA)
    private String accessToken;  // JWT token to use as Authorization header
}

package com.example.bfhl.service;

import com.example.bfhl.dto.GenerateWebhookRequest;
import com.example.bfhl.dto.GenerateWebhookResponse;
import com.example.bfhl.dto.SubmissionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ApiService {
    private static final Logger log = LoggerFactory.getLogger(ApiService.class);

    private final WebClient webClient;

    @Value("${endpoints.generate}")
    private String generateUrl;

    @Value("${endpoints.submit}")
    private String submitUrl;

    public ApiService(WebClient webClient) {
        this.webClient = webClient;
    }

    public GenerateWebhookResponse generateWebhook(GenerateWebhookRequest req) {
        log.info("Calling generateWebhook at {}", generateUrl);

        return webClient.post()
                .uri(generateUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .retrieve()
                .bodyToMono(GenerateWebhookResponse.class)
                .doOnNext(res -> log.info("Received accessToken ({} chars) and webhook",
                        res.getAccessToken() != null ? res.getAccessToken().length() : 0))
                .block();
    }

    public void submitSolution(String jwtAccessToken, String finalSql) {
        log.info("Submitting final SQL to {}", submitUrl);

        webClient.post()
                .uri(submitUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(jwtAccessToken))
                .bodyValue(new SubmissionRequest(finalSql))
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(body -> log.info("Submission response: {}", body))
                .onErrorResume(err -> {
                    log.error("Submission failed: {}", err.getMessage());
                    return Mono.empty();
                })
                .block();
    }
}

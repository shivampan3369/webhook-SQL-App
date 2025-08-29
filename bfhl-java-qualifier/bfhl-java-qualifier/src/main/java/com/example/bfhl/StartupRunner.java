package com.example.bfhl;

import com.example.bfhl.dto.GenerateWebhookRequest;
import com.example.bfhl.dto.GenerateWebhookResponse;
import com.example.bfhl.service.ApiService;
import com.example.bfhl.service.SqlSolver;
import com.example.bfhl.util.SolutionStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(StartupRunner.class);

    @Value("${app.name}")
    private String name;

    @Value("${app.regNo}")
    private String regNo;

    @Value("${app.email}")
    private String email;

    private final ApiService apiService;
    private final SqlSolver sqlSolver;
    private final SolutionStore store;

    public StartupRunner(ApiService apiService, SqlSolver sqlSolver, SolutionStore store) {
        this.apiService = apiService;
        this.sqlSolver = sqlSolver;
        this.store = store;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            // 1) Generate webhook + JWT
            GenerateWebhookRequest req = GenerateWebhookRequest.builder()
                    .name(name)
                    .regNo(regNo)
                    .email(email)
                    .build();

            GenerateWebhookResponse resp = apiService.generateWebhook(req);
            if (resp == null || resp.getAccessToken() == null) {
                log.error("Failed to obtain accessToken/webhook. Response: {}", resp);
                return;
            }

            // 2) Compute final SQL based on regNo (odd/even)
            String finalSql = sqlSolver.computeFinalQuery(regNo);

            // 3) Store locally (optional, for traceability)
            store.save(finalSql);

            // 4) Submit using JWT
            apiService.submitSolution(resp.getAccessToken(), finalSql);

            log.info("Flow completed successfully.");
        } catch (Exception e) {
            log.error("Startup flow failed: {}", e.getMessage(), e);
        }
    }
}

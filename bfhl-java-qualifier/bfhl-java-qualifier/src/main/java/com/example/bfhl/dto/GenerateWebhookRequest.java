package com.example.bfhl.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenerateWebhookRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String regNo;

    @Email
    private String email;
}

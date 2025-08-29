package com.example.bfhl.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class SolutionStore {
    private static final Logger log = LoggerFactory.getLogger(SolutionStore.class);
    private static final Path FILE = Path.of("solution.sql");

    public void save(String sql) {
        try {
            Files.writeString(FILE, sql);
            log.info("Saved solution to {}", FILE.toAbsolutePath());
        } catch (Exception e) {
            log.warn("Failed to save solution locally: {}", e.getMessage());
        }
    }
}

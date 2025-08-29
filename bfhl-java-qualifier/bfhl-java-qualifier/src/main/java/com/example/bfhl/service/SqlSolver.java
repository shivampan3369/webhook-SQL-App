package com.example.bfhl.service;

public interface SqlSolver {
    /**
     * @param regNo full reg no (e.g., "REG12347")
     * @return final SQL query string to submit
     */
    String computeFinalQuery(String regNo);
}

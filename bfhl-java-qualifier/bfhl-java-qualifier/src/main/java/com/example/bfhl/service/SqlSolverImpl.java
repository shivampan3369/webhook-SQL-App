package com.example.bfhl.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Replace the placeholder SQL with the actual solution
 * based on the Google Drive question PDFs:
 * - Odd last two digits -> Question 1
 * - Even last two digits -> Question 2
 */
@Component
public class SqlSolverImpl implements SqlSolver {
    private static final Logger log = LoggerFactory.getLogger(SqlSolverImpl.class);

    @Override
    public String computeFinalQuery(String regNo) {
        String lastTwoDigits = extractLastTwoDigits(regNo);
        boolean isOdd = Integer.parseInt(lastTwoDigits) % 2 == 1;

        log.info("regNo={} -> lastTwoDigits={} -> {}", regNo, lastTwoDigits, isOdd ? "ODD (Q1)" : "EVEN (Q2)");

if (isOdd) {
    // Q1 SQL solution
    return """
        SELECT 
            p.AMOUNT AS SALARY,
            CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME,
            TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE,
            d.DEPARTMENT_NAME
        FROM PAYMENTS p
        JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID
        JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
        WHERE DAY(p.PAYMENT_TIME) <> 1
        ORDER BY p.AMOUNT DESC
        LIMIT 1;
        """;
} else {
    // Q2 SQL placeholder
    return """
        -- Q2 solution goes here
        SELECT 'replace_with_q2_solution' AS answer;
        """;
}

    }

    private String extractLastTwoDigits(String regNo) {
        String digits = regNo.replaceAll("\\D", "");
        if (digits.length() < 2) {
            throw new IllegalArgumentException("regNo must contain at least two digits: " + regNo);
        }
        return digits.substring(digits.length() - 2);
    }
}

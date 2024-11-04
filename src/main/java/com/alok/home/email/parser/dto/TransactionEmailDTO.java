package com.alok.home.email.parser.dto;


import java.time.LocalDateTime;

public record TransactionEmailDTO (
    String id,
    String bank,
    String description,
    double amount,
    boolean verified,
    String verifiedBy,
    boolean accepted,
    LocalDateTime timestamp
) {}

package com.alok.home.email.parser.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class TransactionEmailDTO {

    private String id;
    private String bank;
    private String description;
    private double amount;
    private LocalDateTime timestamp;
}

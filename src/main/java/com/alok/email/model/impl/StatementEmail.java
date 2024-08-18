package com.alok.email.model.impl;

import com.alok.email.model.Email;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@ToString
@Data
public class StatementEmail extends Email {

    public String bank;
    public YearMonth yearMonth;
    public List<Transaction> transactions;

    @Data
    public static class Transaction {
        private double amount;
        private LocalDateTime timestamp;
        public String description;
    }
}

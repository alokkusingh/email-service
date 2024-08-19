package com.alok.home.email.entity.impl;

import com.alok.home.email.entity.Email;
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

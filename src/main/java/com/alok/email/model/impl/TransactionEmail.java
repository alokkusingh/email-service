package com.alok.email.model.impl;

import com.alok.email.model.Email;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
public class TransactionEmail extends Email {

    public double amount;
    public String bank;
    public String description;

    @Builder
    public TransactionEmail(
            String id, String email, String subject, String content, LocalDateTime timestamp,
            double amount, String bank, String description
    ) {

        super(id, email, subject, content, timestamp);
        this.amount = amount;
        this.bank = bank;
        this.description = description;
    }

    @Override
    public String toString() {
        return "TransactionEmail{" +
                "amount=" + amount +
                ", bank='" + bank + '\'' +
                ", description='" + description + '\'' +
                ", id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

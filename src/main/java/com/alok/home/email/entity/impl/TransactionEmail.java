package com.alok.home.email.entity.impl;

import com.alok.home.email.entity.Email;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Table(name = "email_transaction")
@Entity
@Data
public class TransactionEmail extends Email {

    private double amount;
    private String bank;
    private String description;
    private boolean verified;
    private boolean accepted;
    private String verifiedBy;

    @Builder
    public TransactionEmail(
            String id, String email, String subject, String content, LocalDateTime timestamp,
            double amount, String bank, String description, boolean verified, boolean accepted, String verifiedBy
    ) {

        super(id, email, subject, content, timestamp);
        this.amount = amount;
        this.bank = bank;
        this.description = description;
        this.verified = verified;
        this.accepted = accepted;
        this.verifiedBy = verifiedBy;
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

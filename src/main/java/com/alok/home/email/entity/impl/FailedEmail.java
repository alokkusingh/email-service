package com.alok.home.email.entity.impl;

import com.alok.home.email.entity.Email;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Table(name = "email_transaction_failed")
@Entity
@Data
public class FailedEmail extends Email {

    @Column(length = 2000)
    private String content;
    private String error;
    private boolean processed;
    private boolean success;

    @Builder
    public FailedEmail(
            String id, String email, String subject, String content, LocalDateTime timestamp, String error, boolean processed, boolean success
    ) {

        super(id, email, subject, timestamp);
        this.error = error;
        this.processed = processed;
        this.success = success;
        this.content = content;
    }

    @Override
    public String toString() {
        return "FailedEmail{" +
                ", id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

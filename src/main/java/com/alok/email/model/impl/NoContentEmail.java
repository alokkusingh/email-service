package com.alok.email.model.impl;

import com.alok.email.model.Email;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Data
public class NoContentEmail extends Email {

    @Builder
    public NoContentEmail(
            String id, String email, String subject, String content, LocalDateTime timestamp
    ) {
        super(id, email, subject, content, timestamp);
    }
}

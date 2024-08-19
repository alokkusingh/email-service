package com.alok.home.email.entity.impl;

import com.alok.home.email.entity.Email;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Data
public class NoContentEmail extends Email {

    @Builder
    public NoContentEmail(
            String id, String email, String subject, LocalDateTime timestamp
    ) {
        super(id, email, subject, timestamp);
    }
}

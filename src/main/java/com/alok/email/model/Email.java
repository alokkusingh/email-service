package com.alok.email.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public abstract class Email {
    protected String id;
    protected String email;
    protected String subject;
    protected String content;
    protected LocalDateTime timestamp;

}

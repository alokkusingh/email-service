package com.alok.home.email.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class Email {
    @Id
    protected String id;
    protected String email;
    protected String subject;
    protected LocalDateTime timestamp;

}

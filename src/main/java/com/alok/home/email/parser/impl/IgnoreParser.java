package com.alok.home.email.parser.impl;

import com.alok.home.email.entity.impl.NoContentEmail;
import com.alok.home.email.entity.Email;
import com.alok.home.email.parser.Parser;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class IgnoreParser implements Parser {

    @Getter
    private static final IgnoreParser instance = new IgnoreParser();

    @Override
    public Email parseEmail(final String senderEmail, final String subject, final String content) {
        log.warn("IGNORING Mail from: {} with subject: {}", senderEmail, subject);
        return NoContentEmail.builder().build();
    }
}

package com.alok.email.parser.impl;

import com.alok.email.model.impl.NoContentEmail;
import com.alok.email.model.Email;
import com.alok.email.parser.Parser;
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

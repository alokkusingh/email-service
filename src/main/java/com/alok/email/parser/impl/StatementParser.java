package com.alok.email.parser.impl;

import com.alok.email.model.Email;
import com.alok.email.parser.Parser;
import lombok.Getter;

@Getter
public class StatementParser implements Parser {

    @Getter
    private static final StatementParser instance = new StatementParser();

    @Override
    public Email parseEmail(final String senderEmail, final String subject, final String content) {
        return null;
    }
}

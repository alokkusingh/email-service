package com.alok.home.email.parser.impl;

import com.alok.home.email.entity.Email;
import com.alok.home.email.entity.impl.StatementEmail;
import com.alok.home.email.parser.Parser;
import lombok.Getter;

@Getter
public class StatementParser implements Parser {

    @Getter
    private static final StatementParser instance = new StatementParser();

    @Override
    public Email parseEmail(final String senderEmail, final String subject, final String content) {
        // TODO
        return new StatementEmail();
    }
}
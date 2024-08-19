package com.alok.home.email.parser.factory.impl;

import com.alok.home.email.parser.impl.StatementParser;
import com.alok.home.email.parser.factory.EmailParserFactory;
import lombok.Getter;

public class EmailStatementParserFactory implements EmailParserFactory {

    @Getter
    private static EmailStatementParserFactory instance = new EmailStatementParserFactory();
    @Override
    public StatementParser getParser() {
        return StatementParser.getInstance();
    }
}

package com.alok.home.email.parser.factory;

import com.alok.home.email.parser.StatementParser;
import lombok.Getter;

public final class EmailStatementParserFactory implements EmailParserFactory {

    @Getter
    private static EmailStatementParserFactory instance = new EmailStatementParserFactory();
    @Override
    public StatementParser getParser() {
        return StatementParser.getInstance();
    }
}

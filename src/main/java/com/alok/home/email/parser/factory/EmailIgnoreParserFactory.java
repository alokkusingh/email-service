package com.alok.home.email.parser.factory;

import com.alok.home.email.parser.IgnoreParser;
import lombok.Getter;

public final class EmailIgnoreParserFactory implements EmailParserFactory {

    @Getter
    private static EmailIgnoreParserFactory instance = new EmailIgnoreParserFactory();

    @Override
    public IgnoreParser getParser() {
        return IgnoreParser.getInstance();
    }
}

package com.alok.email.parser.factory.impl;

import com.alok.email.parser.impl.IgnoreParser;
import com.alok.email.parser.factory.EmailParserFactory;
import lombok.Getter;

public class EmailIgnoreParserFactory implements EmailParserFactory {

    @Getter
    private static EmailIgnoreParserFactory instance = new EmailIgnoreParserFactory();

    @Override
    public IgnoreParser getParser() {
        return IgnoreParser.getInstance();
    }
}

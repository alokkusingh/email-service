package com.alok.home.email.parser.factory;

import com.alok.home.email.parser.TransactionParser;
import lombok.Getter;

public final class EmailTransactionParserFactory implements EmailParserFactory {

    @Getter
    private static EmailTransactionParserFactory instance = new EmailTransactionParserFactory();

    @Override
    public TransactionParser getParser() {
        return TransactionParser.getInstance();
    }
}

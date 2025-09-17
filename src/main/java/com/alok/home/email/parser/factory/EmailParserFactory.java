package com.alok.home.email.parser.factory;


import com.alok.home.email.parser.Parser;

public sealed interface EmailParserFactory permits EmailTransactionParserFactory, EmailStatementParserFactory, EmailIgnoreParserFactory {

    Parser getParser();
}

package com.alok.home.email.parser.factory;

import com.alok.home.email.enums.EmailType;

public class ParserFactoryUtils {

    private ParserFactoryUtils() {}

    public static EmailParserFactory getEmailParserFactory(final String emailId, final String subject) {
        return switch (EmailUtils.getEmailType(emailId, subject)) {
            case TRANSACTION -> EmailTransactionParserFactory.getInstance();
            case STATEMENT, UNKNOWN -> EmailIgnoreParserFactory.getInstance();
        };
    }


    public static class EmailUtils {

        private EmailUtils() {}

        public static EmailType getEmailType(final String emailId, final String subject) {
            return switch (emailId.toLowerCase()) {
               case "alerts@hdfcbank.net", "onlinesbicard@sbicard.com", "alerts@axisbank.com" ->  EmailType.TRANSACTION;
               default -> EmailType.UNKNOWN;
            };
        }
    }
}

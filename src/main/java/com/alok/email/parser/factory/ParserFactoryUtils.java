package com.alok.email.parser.factory;

import com.alok.email.enums.EmailType;
import com.alok.email.parser.factory.impl.EmailIgnoreParserFactory;
import com.alok.email.parser.factory.impl.EmailTransactionParserFactory;

public class ParserFactoryUtils {

    private ParserFactoryUtils() {}

    public static EmailParserFactory getParser(final String emailId, final String subject) {
        switch (EmailUtils.getEmailType(emailId, subject)) {
            case TRANSACTION -> {
                return EmailTransactionParserFactory.getInstance();
            }
            case STATEMENT -> {
                // TODO
                return EmailIgnoreParserFactory.getInstance();
            }
            default -> {
                return EmailIgnoreParserFactory.getInstance();
            }
        }
    }


    public static class EmailUtils {

        private EmailUtils() {}

        public static EmailType getEmailType(final String emailId, final String subject) {
            if (emailId.equalsIgnoreCase("alerts@hdfcbank.net")) {
                return EmailType.TRANSACTION;
            }

            if (emailId.equalsIgnoreCase("onlinesbicard@sbicard.com")) {
                return EmailType.TRANSACTION;
            }

            if (emailId.equalsIgnoreCase("alerts@axisbank.com")) {
                return EmailType.TRANSACTION;
            }

            return EmailType.UNKNOWN;
        }

    }
}

package com.alok.home.email.parser.factory;

import com.alok.home.email.enums.EmailType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserFactoryUtilsTest {

    @Test
    void returnsTransactionFactoryForKnownEmails() {
        String[] knownEmails = {
                "alerts@hdfcbank.net",
                "alerts@hdfcbank.bank.in",
                "alerts@axis.bank.in",
                "onlinesbicard@sbicard.com",
                "alerts@axisbank.com"
        };

        for (String email : knownEmails) {
            EmailParserFactory factory = ParserFactoryUtils.getEmailParserFactory(email, "some subject");
            assertNotNull(factory, "Factory should not be null for: " + email);
            assertTrue(factory instanceof EmailTransactionParserFactory,
                    "Expected EmailTransactionParserFactory for: " + email);
        }
    }

    @Test
    void emailMatchingIsCaseInsensitive() {
        EmailParserFactory factory = ParserFactoryUtils.getEmailParserFactory("ALERTS@HDFCBANK.NET", "subject");
        assertNotNull(factory);
        assertTrue(factory instanceof EmailTransactionParserFactory);
    }

    @Test
    void returnsIgnoreFactoryForUnknownEmail() {
        EmailParserFactory factory = ParserFactoryUtils.getEmailParserFactory("unknown@bank.com", "random subject");
        assertNotNull(factory);
        assertTrue(factory instanceof EmailIgnoreParserFactory);
    }

    @Test
    void subjectDoesNotAffectEmailTypeResolution() {
        // Even with a subject that looks like a transaction, email determines the type
        EmailParserFactory factory = ParserFactoryUtils.getEmailParserFactory(
                "unknown@bank.com",
                "Alert :  Update on your HDFC Bank Credit Card"
        );
        assertNotNull(factory);
        assertTrue(factory instanceof EmailIgnoreParserFactory);
    }

    @Test
    void emailUtilsGetEmailTypeDirectly() {
        assertEquals(EmailType.TRANSACTION,
                ParserFactoryUtils.EmailUtils.getEmailType("alerts@hdfcbank.net", "ignored"));
        assertEquals(EmailType.UNKNOWN,
                ParserFactoryUtils.EmailUtils.getEmailType("not-in-list@bank.com", "ignored"));
    }
}
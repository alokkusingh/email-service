package com.alok.home.email.parser.retriver.transactiondate.impl;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HdfcCCTransactionDateRetrieverTest {

    private final HdfcCCTransactionDateRetriever retriever = new HdfcCCTransactionDateRetriever();

    @Test
    public void testRetrieveWithPattern1() {
        String content = "Thank you for using your HDFC Bank Credit Card ending XXXX for Rs 500.00 at CRED_FASTAG on 16-08-2024 14:00:55. Authorization code";
        LocalDateTime expected = LocalDateTime.of(2024, 8, 16, 14, 0, 55);
        LocalDateTime result = retriever.retrieve(content);
        assertEquals(expected, result);
    }

    @Test
    public void testRetrieveWithPattern2() {
        String content = """
                HDFC BANK Dear Customer, Thank you for using HDFC Bank Card XX5464 for Rs. 594.0 at AMAZON on 06-04-2025 08:41:49 Authorization code:- 086448 Please note that this transaction was conducted without OTP /PIN. If you have not authorized the above transaction, please call on 18002586161 Warm regards, HDFC Bank (This is a system generated mail and should not be replied to) For more details on Service charges and Fees, click here. © HDFC Bank
                """;
        LocalDateTime expected = LocalDateTime.of(2025, 4, 6, 8, 41, 49);
        LocalDateTime result = retriever.retrieve(content);
        assertEquals(expected, result);
    }

    @Test
    public void testRetrieveWithNoPattern() {
        String content = "Some random content without any matching pattern";
        LocalDateTime result = retriever.retrieve(content);
        assertNotNull(result);
    }
}
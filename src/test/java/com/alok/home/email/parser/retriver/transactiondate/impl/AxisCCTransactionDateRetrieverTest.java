package com.alok.home.email.parser.retriver.transactiondate.impl;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AxisCCTransactionDateRetrieverTest {

    @Test
    void testRetrieve_ValidContent_ReturnsParsedDate() {
        AxisCCTransactionDateRetriever retriever = new AxisCCTransactionDateRetriever();
        String content = "Thank you for using your credit card no. XX1234 INR 1300 at GOOGLEPLAY on 16-08-2024 09:23:59 IST.";
        LocalDateTime expected = LocalDateTime.of(2024, 8, 16, 9, 23, 59);

        LocalDateTime result = retriever.retrieve(content);

        assertEquals(expected, result);
    }

    @Test
    void testRetrieve_ValidContent_ReturnsParsedDate_2() {
        AxisCCTransactionDateRetriever retriever = new AxisCCTransactionDateRetriever();
        String content = """
                14-09-2025
                
                Dear Alok Singh,
                
                
                Here's the summary of your Axis Bank Credit Card Transaction:
                
                Transaction Amount:
                INR 2526
                
                Merchant Name:
                RSP*NYKAA V
                
                Axis Bank Credit Card No.
                XX617
                
                Date & Time:
                14-09-2025, 11:24:25 IST
                
                Available Limit*:
                INR 1414
                
                Total Credit Limit*:
                INR 1440
                *The information above includes the available and total credit limit across all of your Axis Bank credit cards.
                If this transaction was not intiated by you:
                
                """;
        LocalDateTime expected = LocalDateTime.of(2025, 9, 14, 11, 24, 25);

        LocalDateTime result = retriever.retrieve(content);

        assertEquals(expected, result);
    }

    @Test
    void testRetrieve_InvalidContent_ReturnsNow() {
        AxisCCTransactionDateRetriever retriever = new AxisCCTransactionDateRetriever();
        String content = "Some unrelated email content.";

        LocalDateTime before = LocalDateTime.now();
        LocalDateTime result = retriever.retrieve(content);
        LocalDateTime after = LocalDateTime.now();

        assertTrue(result.isAfter(before) && result.isBefore(after.plusSeconds(1)));
    }
}
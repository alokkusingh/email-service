package com.alok.home.email.parser.retriver.amount.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AxisCCAmountRetrieverTest {

    @Test
    void testRetrieve_ValidContent() {
        AxisCCAmountRetriever retriever = new AxisCCAmountRetriever();
        String content = "Thank you for using your credit card no. XX1234 for INR 1,300.50 at GOOGLEPLAY on 16-08-2024 09:23:59 IST.";
        double amount = retriever.retrieve(content);
        assertEquals(1300.50, amount);
    }

    @Test
    void testRetrieve_ValidContent_2() {
        AxisCCAmountRetriever retriever = new AxisCCAmountRetriever();
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
        double amount = retriever.retrieve(content);
        assertEquals(2526.00, amount);
    }

    @Test
    void testRetrieve_InvalidContent() {
        AxisCCAmountRetriever retriever = new AxisCCAmountRetriever();
        String content = "No amount present here.";
        double amount = retriever.retrieve(content);
        assertEquals(0.0, amount);
    }

    @Test
    void testRetrieve_MalformedAmount() {
        AxisCCAmountRetriever retriever = new AxisCCAmountRetriever();
        String content = "Thank you for using your credit card no. XX1234 for INR ABCD at GOOGLEPLAY on 16-08-2024 09:23:59 IST.";
        double amount = retriever.retrieve(content);
        assertEquals(0.0, amount);
    }
}
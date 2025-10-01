package com.alok.home.email.parser.retriver.remarks.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AxisCCRemarksRetrieverTest {

    @Test
    void testRetrieve_withValidContent_returnsExpectedSubstring() {
        AxisCCRemarksRetriever retriever = new AxisCCRemarksRetriever();
        String content = "Thank you for using your credit card no. XX1234 for INR 1300 at GOOGLEPLAY on 16-08-2024 09:23:59 IST.";
        String expected = "INR 1300 at GOOGLEPLAY on 16-08-2024 09:23:59";
        String actual = retriever.retrieve(content);
        assertEquals(expected, actual);
    }

    @Test
    void testRetrieve_withValidContent_returnsExpectedSubstring_2() {
        AxisCCRemarksRetriever retriever = new AxisCCRemarksRetriever();
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
        String expected = "Merchant Name: RSP*NYKAA V";
        String actual = retriever.retrieve(content);
        assertEquals(expected, actual);
    }

    @Test
    void testRetrieve_withInvalidContent_returnsEmptyString() {
        AxisCCRemarksRetriever retriever = new AxisCCRemarksRetriever();
        String content = "This is some unrelated text.";
        String actual = retriever.retrieve(content);
        assertEquals("", actual);
    }
}
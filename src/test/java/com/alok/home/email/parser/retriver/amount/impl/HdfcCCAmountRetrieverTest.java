package com.alok.home.email.parser.retriver.amount.impl;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HdfcCCAmountRetrieverTest {

    private final HdfcCCAmountRetriever retriever = new HdfcCCAmountRetriever();

    @Test
    void testRetrieveWithPattern1() {
        String content = "Thank you for using your HDFC Bank Credit Card ending XXXX for Rs 1,234.56 at CRED_FASTAG on 16-08-2024 14:00:55. Authorization code";
        double result = retriever.retrieve(content);
        assertEquals(1234.56, result);
    }

    @Test
    void testRetrieveWithPattern2() {
        String content = "Thank you for using HDFC Bank Card ...... for Rs. 987.65 at CRED_FASTAG on 16-08-2024 14:00:55";
        double result = retriever.retrieve(content);
        assertEquals(987.65, result);
    }

    @Test
    void testRetrieveWithPattern3() {
        String content = "Rs.755.10 is debited from your HDFC Bank Credit Card ending 123456 towards AMAZON";
        double result = retriever.retrieve(content);
        assertEquals(755.10, result);
    }

    @Test
    void testRetrieveWithPattern3_2() {
        String content = """
                HDFC BANK Dear Customer, Greetings from HDFC Bank! 
                Rs.218.00 is debited from your HDFC Bank Credit Card ending 546 towards FLIPKART INTERNET PVT on 30 Aug, 2025 at 09:22:21. 
                If you did not authorize this transaction, please report it immediately at: 1. When in India (Toll free): 1800 258 6161 2. When abroad: +9122 61606160 For more details on this transaction, visit HDFC Bank MyCards. We're here to support you in every step of the way. Warm regards, HDFC Bank This is a system-generated email. Please do not reply. 
                For more details on Service charges and Fees, click here. © HDFC Bank
                """;
        double result = retriever.retrieve(content);
        assertEquals(218, result);
    }

    @Test
    void testRetrieveWithPattern4() {
        String content = """
                Dear Customer,
                
                          Greetings from HDFC Bank.
                
                          We would like to inform you that Rs. 5071.00 has been debited from your HDFC Bank Credit Card ending 5464 towards PYU*FLIPKART INTERNET on 17 May, 2026 at 16:33:56.
                          To check your available balance, outstanding amount, or view recent transactions, you may use:
                          Mycards:https://mycards.hdfc.bank.in
                          WhatsApp Banking:https://hdfcbk.io/HDFCBK/K/DUvfZ20acT6
                
                          Important Note:
                          If you did not authorise this transaction, please act immediately. Your safety is our top priority, and we are here to help.
                """;
        double result = retriever.retrieve(content);
        assertEquals(5071, result);
    }

    @Test
    void testRetrieveWithNoPattern() {
        String content = "Some unrelated message";
        double result = retriever.retrieve(content);
        assertEquals(0.0, result);
    }
}

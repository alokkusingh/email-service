// java
package com.alok.home.email.parser.retriver.remarks.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HdfcSBRemarksRetrieverTest {

    private final HdfcSBRemarksRetriever retriever = new HdfcSBRemarksRetriever();

    @Test
    void testRetrieve_pattern1_returnsExpectedSubstring() {
        String content = "Rs.165.00 has been debited from account **XXXX to VPA paytmqr69gdim0mu8@paytm on 18-08-24. Your UPI transaction reference";
        String expected = "165.00 has been debited from account **XXXX to VPA paytmqr69gdim0mu8@paytm on 18-08-24.";
        String actual = retriever.retrieve(content);
        assertEquals(expected, actual);
    }

    @Test
    void testRetrieve_pattern2_returnsExpectedSubstring() {
        String content = "Your A/c xxxxxxxxxx.... is debited for INR 2,526 on 14-09-2025 and A/c xxxxxxx.... is credited";
        String expected = "A/c xxxxxxxxxx.... is debited for INR 2,526 on 14-09-2025";
        String actual = retriever.retrieve(content);
        assertEquals(expected, actual);
    }

    @Test
    void testRetrieve_pattern3_returnsExpectedSubstring() {
        String content = "Alert: Cheque No. ............ of INR 2,000.75, has been received in clearing on 14-09-2025 and debited from your Account XX.....";
        String expected = "Cheque No. ............ of INR 2,000.75, has been received in clearing on 14-09-2025 and debited";
        String actual = retriever.retrieve(content);
        assertEquals(expected, actual);
    }

    @Test
    void testRetrieve_pattern4_returnsExpectedSubstring() {
        String content = """
                Dear Customer,
                
                Greetings from HDFC Bank!
                
                Rs.110.00 is debited from your account ending 0531 towards VPA compassindiafoodserrzb.rzp@mairtel (COMPASS INDIA FOOD SERVICES PR) on 15-05-26.
                
                UPI transaction reference no.: 123159568107.
                
                If you did not authorize this transaction, please report it immediately at:
                a. When in India (Toll free): 1800 258 6161
                b. When abroad: 9122 61606160
                c. Or SMS 'BLOCK UPI' to 7308080808.
                
                We're here to support you in every step of the way.
                
                Warm regards,
                HDFC Bank
                """;
        String expected = "VPA compassindiafoodserrzb.rzp@mairtel (COMPASS INDIA FOOD SERVICES PR)";
        String actual = retriever.retrieve(content);
        assertEquals(expected, actual);
    }

    @Test
    void testRetrieve_noMatch_returnsEmptyString() {
        String content = "This is some unrelated text.";
        String actual = retriever.retrieve(content);
        assertEquals("", actual);
    }
}

package com.alok.home.email.parser.retriver.amount.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HdfcSBAmountRetrieverTest {

    private final HdfcSBAmountRetriever retriever = new HdfcSBAmountRetriever();

    @Test
    void testRetrieve_Pattern1_RsWithDecimal() {
        String content = "Rs.165.00 has been debited from account **XXXX to VPA paytmqr69gdim0mu8@paytm on 18-08-24. Your UPI transaction reference";
        double amount = retriever.retrieve(content);
        assertEquals(165.00, amount, 0.001);
    }

    @Test
    void testRetrieve_Pattern2_INRWithCommas_NoDecimal() {
        String content = "Your A/c xxxxxxxxxx.... is debited for INR 2,526 on 14-09-25 and A/c xxxxxxx.... is credited";
        double amount = retriever.retrieve(content);
        assertEquals(2526.00, amount, 0.001);
    }

    @Test
    void testRetrieve_Pattern3_AlertCheque_WithDecimal() {
        String content = "Alert: Cheque No. ............ of INR 2,000.75, has been received in clearing on ..-...-.. and debited from your Account XX.....";
        double amount = retriever.retrieve(content);
        assertEquals(2000.75, amount, 0.001);
    }

    @Test
    void testRetrieve_Pattern4() {
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
        double amount = retriever.retrieve(content);
        assertEquals(110, amount, 0.001);
    }
    @Test
    void testRetrieve_NoMatch_ReturnsZero() {
        String content = "This message contains no monetary amount information.";
        double amount = retriever.retrieve(content);
        assertEquals(0.0, amount, 0.001);
    }
}
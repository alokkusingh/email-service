package com.alok.home.email.parser;

import com.alok.home.email.entity.Email;
import com.alok.home.email.entity.impl.FailedEmail;
import com.alok.home.email.entity.impl.TransactionEmail;
import com.alok.home.email.enums.EmailTransactionType;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class TransactionParserTest {

    @Test
    void testParseEmail_success() {
        String sender = "alerts@hdfcbank.net";
        String subject = "You have done a UPI txn. Check details!";
        String content = "Rs.165.00 has been debited from account **XXXX to VPA paytmqr69gdim0mu8@paytm on 18-08-24. Your UPI transaction reference";

        Email result = TransactionParser.getInstance().parseEmail(sender, subject, content);

        assertTrue(result instanceof TransactionEmail, "Expected TransactionEmail on successful parse");
        TransactionEmail tx = (TransactionEmail) result;

        assertEquals(sender, tx.getEmail());
        assertEquals(subject, tx.getSubject());
        assertEquals(EmailTransactionType.HDFC_SB_TRANS.name(), tx.getBank());
        assertEquals(165.00, tx.getAmount(), 0.001);
        assertNotNull(tx.getDescription());
        assertFalse(tx.getDescription().isEmpty());
        assertNotNull(tx.getTimestamp());
    }

    @Test
    void testParseEmail_onParserException_returnsFailedEmail() {
        String sender = "alerts@hdfcbank.net";
        String subject = "You have done a UPI txn. Check details!";
        String content = "some content";

        try (MockedStatic<ParserUtils> mocked = Mockito.mockStatic(ParserUtils.class)) {
            // Ensure getTransactionType returns a valid type (called before amount retriever)
            mocked.when(() -> ParserUtils.getTransactionType(Mockito.anyString(), Mockito.anyString()))
                    .thenReturn(EmailTransactionType.HDFC_SB_TRANS);

            // Simulate an exception when amount retriever is requested
            mocked.when(() -> ParserUtils.getAmountRetriever(Mockito.anyString(), Mockito.anyString()))
                    .thenThrow(new RuntimeException("simulated failure"));

            Email result = TransactionParser.getInstance().parseEmail(sender, subject, content);

            assertTrue(result instanceof FailedEmail, "Expected FailedEmail when ParserUtils throws");
            FailedEmail failed = (FailedEmail) result;
            assertEquals(sender, failed.getEmail());
            assertEquals(subject, failed.getSubject());
            assertTrue(failed.getError().contains("simulated failure"));
        }
    }

    @Test
    void testParseEmail_success_HDFC_SB_UPI() {
        String sender = "alerts@hdfcbank.bank.in";
        String subject = "❗  You have done a UPI txn. Check details!";
        String content = """
                Dear Customer,
                
                Greetings from HDFC Bank!
                
                Rs.137.00 is debited from your account ending 0531 towards VPA paytm-56505013@ptybl (Flipkart Payments) on 18-05-26.
                
                UPI transaction reference no.: 180630950356.
                
                If you did not authorize this transaction, please report it immediately at:
                a. When in India (Toll free): 1800 258 6161
                b. When abroad: 9122 61606160
                c. Or SMS 'BLOCK UPI' to 7308080808.
                
                We're here to support you in every step of the way.
                
                Warm regards,
                HDFC Bank
                """;

        Email result = TransactionParser.getInstance().parseEmail(sender, subject, content);

        assertTrue(result instanceof TransactionEmail, "Expected TransactionEmail on successful parse");
        TransactionEmail tx = (TransactionEmail) result;

        assertEquals(sender, tx.getEmail());
        assertEquals(subject, tx.getSubject());
        assertEquals(EmailTransactionType.HDFC_SB_TRANS.name(), tx.getBank());
        assertEquals(137.00, tx.getAmount(), 0.001);
        assertNotNull(tx.getDescription());
        assertFalse(tx.getDescription().isEmpty());
        assertNotNull(tx.getTimestamp());
        assertEquals("VPA paytm-56505013@ptybl (Flipkart Payments)", tx.getDescription());
    }

    @Test
    void testParseEmail_success_HDFC_SB_UPI2() {
        String sender = "alerts@hdfcbank.bank.in";
        String subject = "❗  You have done a UPI txn. Check details!";
        String content = """
                Dear Customer,
                
                         Greetings from HDFC Bank!
                
                         Rs.95.00 is debited from your account ending 0531 towards VPA compassindiafoodserrzb.rzp@mairtel (COMPASS INDIA FOOD SERVICES PR) on 19-05-26.
                
                         UPI transaction reference no.: 123367520968.
                
                         If you did not authorize this transaction, please report it immediately at:
                         a. When in India (Toll free): 1800 258 6161
                         b. When abroad: 9122 61606160
                         c. Or SMS 'BLOCK UPI' to 7308080808.
                
                         We're here to support you in every step of the way.
                
                         Warm regards,
                         HDFC Bank
                """;

        Email result = TransactionParser.getInstance().parseEmail(sender, subject, content);

        assertTrue(result instanceof TransactionEmail, "Expected TransactionEmail on successful parse");
        TransactionEmail tx = (TransactionEmail) result;

        assertEquals(sender, tx.getEmail());
        assertEquals(subject, tx.getSubject());
        assertEquals(EmailTransactionType.HDFC_SB_TRANS.name(), tx.getBank());
        assertEquals(95.00, tx.getAmount(), 0.001);
        assertNotNull(tx.getDescription());
        assertFalse(tx.getDescription().isEmpty());
        assertNotNull(tx.getTimestamp());
        assertEquals("VPA compassindiafoodserrzb.rzp@mairtel (COMPASS INDIA FOOD SERVICES PR)", tx.getDescription());
    }

    @Test
    void testParseEmail_success_HDFC_CC() {
        String sender = "alerts@hdfcbank.bank.in";
        String subject = "A payment was made using your Credit Card";
        String content = """
                Dear Customer,
                
                         Greetings from HDFC Bank.
                
                         We would like to inform you that Rs. 1406.00 has been debited from your HDFC Bank Credit Card ending 5464 towards Amazon Seller Services on 17 May, 2026 at 17:59:13.
                         To check your available balance, outstanding amount, or view recent transactions, you may use:
                         Mycards:https://mycards.hdfc.bank.in
                         WhatsApp Banking:https://hdfcbk.io/HDFCBK/K/DUvfZ20acT6
                
                         Important Note:
                         If you did not authorise this transaction, please act immediately. Your safety is our top priority, and we are here to help.
                """;

        Email result = TransactionParser.getInstance().parseEmail(sender, subject, content);

        assertTrue(result instanceof TransactionEmail, "Expected TransactionEmail on successful parse");
        TransactionEmail tx = (TransactionEmail) result;

        assertEquals(sender, tx.getEmail());
        assertEquals(subject, tx.getSubject());
        assertEquals(EmailTransactionType.HDFC_CC_TRANS.name(), tx.getBank());
        assertEquals(1406.00, tx.getAmount(), 0.001);
        assertNotNull(tx.getDescription());
        assertFalse(tx.getDescription().isEmpty());
        assertNotNull(tx.getTimestamp());
        assertEquals("Amazon Seller Services on 17 May, 2026 at 17:59:13.", tx.getDescription());
    }
}

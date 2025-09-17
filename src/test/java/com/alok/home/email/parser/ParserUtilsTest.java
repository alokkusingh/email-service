package com.alok.home.email.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.alok.home.email.enums.EmailTransactionType;
import com.alok.home.email.parser.ParserUtils;

class ParserUtilsTest {

    @Test
    void testHdfcCcTransExactSubject() {
        EmailTransactionType type = ParserUtils.getTransactionType(
                "alerts@hdfcbank.net",
                "Alert :  Update on your HDFC Bank Credit Card"
        );
        assertEquals(EmailTransactionType.HDFC_CC_TRANS, type);
    }

    @Test
    void testHdfcCcTransContains546() {
        EmailTransactionType type = ParserUtils.getTransactionType(
                "alerts@hdfcbank.net",
                "Rs.419.00 debited via Credit Card **546"
        );
        assertEquals(EmailTransactionType.HDFC_CC_TRANS, type);
    }

    @Test
    void testHdfcSbTransUpiTxn() {
        EmailTransactionType type = ParserUtils.getTransactionType(
                "alerts@hdfcbank.net",
                "You have done a UPI txn"
        );
        assertEquals(EmailTransactionType.HDFC_SB_TRANS, type);
    }

    @Test
    void testHdfcSbTransAccountUpdate() {
        EmailTransactionType type = ParserUtils.getTransactionType(
                "alerts@hdfcbank.net",
                "View: Account update for your HDFC Bank"
        );
        assertEquals(EmailTransactionType.HDFC_SB_TRANS, type);
    }

    @Test
    void testHdfcSbTransExactUpiSubject() {
        EmailTransactionType type = ParserUtils.getTransactionType(
                "alerts@hdfcbank.net",
                "You have done a UPI txn. Check details!"
        );
        assertEquals(EmailTransactionType.HDFC_SB_TRANS, type);
    }

    @Test
    void testSbiCcTrans() {
        EmailTransactionType type = ParserUtils.getTransactionType(
                "onlinesbicard@sbicard.com",
                "Transaction Alert from SBI Card"
        );
        assertEquals(EmailTransactionType.SBI_CC_TRANS, type);
    }

    @Test
    void testAxisCcTrans() {
        EmailTransactionType type = ParserUtils.getTransactionType(
                "alerts@axisbank.com",
                "Transaction alert on Axis Bank Credit Card"
        );
        assertEquals(EmailTransactionType.AXIS_CC_TRANS, type);
    }

    @Test
    void testUnknownTrans() {
        EmailTransactionType type = ParserUtils.getTransactionType(
                "unknown@bank.com",
                "Some random subject"
        );
        assertEquals(EmailTransactionType.UNKNOWN_TRANS, type);
    }
}
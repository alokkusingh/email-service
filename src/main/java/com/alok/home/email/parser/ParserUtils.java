package com.alok.home.email.parser;

import com.alok.home.email.enums.EmailTransactionType;
import com.alok.home.email.parser.retriver.amount.impl.*;
import com.alok.home.email.parser.retriver.remarks.impl.*;
import com.alok.home.email.parser.retriver.remarks.RemarksRetriever;
import com.alok.home.email.parser.retriver.transactiondate.impl.*;
import com.alok.home.email.parser.retriver.transactiondate.TransactionDateRetriever;
import com.alok.home.email.parser.retriver.amount.AmountRetriever;

public class ParserUtils {

    private ParserUtils() {}

    public static AmountRetriever getAmountRetriever(String senderEmail, String subject) {
        switch (getTransactionType(senderEmail, subject)) {
            case HDFC_CC_TRANS -> {
                return new HdfcCCAmountRetriever();
            }
            case HDFC_SB_TRANS -> {
                return new HdfcSBAmountRetriever();
            }
            case SBI_CC_TRANS -> {
                return new SbiCCAmountRetriever();
            }
            case AXIS_CC_TRANS -> {
                return new AxisCCAmountRetriever();
            }
            default -> {
                return new UnknownAmountRetriever();
            }
        }
    }

    public static RemarksRetriever getRemarksRetriever(String senderEmail, String subject) {
        switch (getTransactionType(senderEmail, subject)) {
            case HDFC_CC_TRANS -> {
                return new HdfcCCRemarksRetriever();
            }
            case HDFC_SB_TRANS -> {
                return new HdfcSBRemarksRetriever();
            }
            case SBI_CC_TRANS -> {
                return new SbiCCRemarksRetriever();
            }
            case AXIS_CC_TRANS -> {
                return new AxisCCRemarksRetriever();
            }
            default -> {
                return new UnknownRemarksRetriever();
            }
        }
    }

    public static TransactionDateRetriever getTransactionDateRetriever(String senderEmail, String subject) {
        switch (getTransactionType(senderEmail, subject)) {
            case HDFC_CC_TRANS -> {
                return new HdfcCCTransactionDateRetriever();
            }
            case HDFC_SB_TRANS -> {
                return new HdfcSBTransactionDateRetriever();
            }
            case SBI_CC_TRANS -> {
                return new SbiCCTransactionDateRetriever();
            }
            case AXIS_CC_TRANS -> {
                return new AxisCCTransactionDateRetriever();
            }
            default -> {
                return new UnknownTransactionDateRetriever();
            }
        }
    }

    public static EmailTransactionType getTransactionType(String senderEmail, String subject) {
        if (senderEmail.equalsIgnoreCase("alerts@hdfcbank.net")
                && subject.equalsIgnoreCase("Alert :  Update on your HDFC Bank Credit Card")) {
            return EmailTransactionType.HDFC_CC_TRANS;
        }

        if (senderEmail.equalsIgnoreCase("alerts@hdfcbank.net")
                && subject.contains("You have done a UPI txn")) {
            return EmailTransactionType.HDFC_SB_TRANS;
        }

        if (senderEmail.equalsIgnoreCase("alerts@hdfcbank.net")
                && subject.contains("View: Account update for your HDFC Bank")) {
            return EmailTransactionType.HDFC_SB_TRANS;
        }

        if (senderEmail.equalsIgnoreCase("alerts@hdfcbank.net")
                && (subject.equalsIgnoreCase("You have done a UPI txn. Check details!"))) {
            return EmailTransactionType.HDFC_SB_TRANS;
        }

        if (senderEmail.equalsIgnoreCase("onlinesbicard@sbicard.com")
                && (subject.equalsIgnoreCase("Transaction Alert from SBI Card"))) {
            return EmailTransactionType.SBI_CC_TRANS;
        }

        if (senderEmail.equalsIgnoreCase("alerts@axisbank.com")
                && (subject.contains("Transaction alert on Axis Bank Credit Card"))) {
            return EmailTransactionType.AXIS_CC_TRANS;
        }

        return EmailTransactionType.UNKNOWN_TRANS;
    }
}

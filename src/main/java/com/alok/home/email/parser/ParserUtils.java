package com.alok.home.email.parser;

import com.alok.home.email.entity.Email;
import com.alok.home.email.enums.EmailTransactionType;
import com.alok.home.email.parser.factory.ParserFactoryUtils;
import com.alok.home.email.parser.retriver.amount.impl.*;
import com.alok.home.email.parser.retriver.remarks.impl.*;
import com.alok.home.email.parser.retriver.remarks.RemarksRetriever;
import com.alok.home.email.parser.retriver.transactiondate.impl.*;
import com.alok.home.email.parser.retriver.transactiondate.TransactionDateRetriever;
import com.alok.home.email.parser.retriver.amount.AmountRetriever;

public class ParserUtils {

    private ParserUtils() {}

    public static AmountRetriever getAmountRetriever(String senderEmail, String subject) {
        return switch (getTransactionType(senderEmail, subject)) {
            case HDFC_CC_TRANS -> new HdfcCCAmountRetriever();
            case HDFC_SB_TRANS -> new HdfcSBAmountRetriever();
            case SBI_CC_TRANS -> new SbiCCAmountRetriever();
            case AXIS_CC_TRANS -> new AxisCCAmountRetriever();
            default -> new UnknownAmountRetriever();
        };
    }

    public static RemarksRetriever getRemarksRetriever(String senderEmail, String subject) {
        return switch (getTransactionType(senderEmail, subject)) {
            case HDFC_CC_TRANS -> new HdfcCCRemarksRetriever();
            case HDFC_SB_TRANS -> new HdfcSBRemarksRetriever();
            case SBI_CC_TRANS -> new SbiCCRemarksRetriever();
            case AXIS_CC_TRANS -> new AxisCCRemarksRetriever();
            default -> new UnknownRemarksRetriever();
        };
    }

    public static TransactionDateRetriever getTransactionDateRetriever(String senderEmail, String subject) {
        return switch (getTransactionType(senderEmail, subject)) {
            case HDFC_CC_TRANS -> new HdfcCCTransactionDateRetriever();
            case HDFC_SB_TRANS -> new HdfcSBTransactionDateRetriever();
            case SBI_CC_TRANS -> new SbiCCTransactionDateRetriever();
            case AXIS_CC_TRANS -> new AxisCCTransactionDateRetriever();
            default -> new UnknownTransactionDateRetriever();
        };
    }

    public static EmailTransactionType getTransactionType(String senderEmail, String subject) {
        if (senderEmail.equalsIgnoreCase("alerts@hdfcbank.net")
                && subject.equalsIgnoreCase("Alert :  Update on your HDFC Bank Credit Card")) {
            return EmailTransactionType.HDFC_CC_TRANS;
        }

        if (senderEmail.equalsIgnoreCase("alerts@hdfcbank.net")
                && subject.contains("debited via Credit Card **546")) {
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

    public static Email parseEmail(String senderEmailAddress, String subject, String content) {
        return ParserFactoryUtils.getEmailParserFactory(senderEmailAddress, subject)
                .getParser()
                .parseEmail(senderEmailAddress, subject, content);
    }
}

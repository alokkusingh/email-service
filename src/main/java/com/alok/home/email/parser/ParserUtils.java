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
        return switch (senderEmail.toLowerCase()) {
            case "alerts@hdfcbank.net" -> {
                if (subject.equalsIgnoreCase("Alert :  Update on your HDFC Bank Credit Card") ||
                        subject.contains("debited via Credit Card **546")) {
                    yield EmailTransactionType.HDFC_CC_TRANS;
                } else if (subject.contains("You have done a UPI txn") ||
                        subject.contains("View: Account update for your HDFC Bank") ||
                        subject.equalsIgnoreCase("You have done a UPI txn. Check details!")) {
                    yield EmailTransactionType.HDFC_SB_TRANS;
                } else {
                    yield EmailTransactionType.UNKNOWN_TRANS;
                }
            }
            case "onlinesbicard@sbicard.com" -> {
                if (subject.equalsIgnoreCase("Transaction Alert from SBI Card")) {
                    yield EmailTransactionType.SBI_CC_TRANS;
                } else {
                    yield EmailTransactionType.UNKNOWN_TRANS;
                }
            }
            case "alerts@axisbank.com" -> {
                if (subject.contains("Transaction alert on Axis Bank Credit Card")) {
                    yield EmailTransactionType.AXIS_CC_TRANS;
                } else if (subject.contains("spent on credit card no. XX617")) {
                    yield EmailTransactionType.AXIS_CC_TRANS;
                } else {
                    yield EmailTransactionType.UNKNOWN_TRANS;
                }
            }
            default -> EmailTransactionType.UNKNOWN_TRANS;
        };
    }

    public static Email parseEmail(String senderEmailAddress, String subject, String content) {
        return ParserFactoryUtils.getEmailParserFactory(senderEmailAddress, subject)
                .getParser()
                .parseEmail(senderEmailAddress, subject, content);
    }
}

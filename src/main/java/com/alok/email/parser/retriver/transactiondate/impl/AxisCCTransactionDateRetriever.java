package com.alok.email.parser.retriver.transactiondate.impl;

import com.alok.email.parser.retriver.transactiondate.TransactionDateRetriever;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AxisCCTransactionDateRetriever implements TransactionDateRetriever {
    @Override
    public LocalDateTime retrieve(String content) {

        // Thank you for using your credit card no. XXYYYY for INR 1300 at GOOGLEPLAY on 16-08-2024 09:23:59 IST.
        Pattern pattern = Pattern.compile("Thank you for using your credit card no. XX.... INR .* on (.*) IST.");
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            String substring = matcher.group(1);
            return LocalDateTime.parse(substring, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        }

        return LocalDateTime.now();
    }
}


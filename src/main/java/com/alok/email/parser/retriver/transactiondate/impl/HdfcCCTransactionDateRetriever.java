package com.alok.email.parser.retriver.transactiondate.impl;

import com.alok.email.parser.retriver.transactiondate.TransactionDateRetriever;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HdfcCCTransactionDateRetriever implements TransactionDateRetriever {
    @Override
    public LocalDateTime retrieve(String content) {

        // Thank you for using your HDFC Bank Credit Card ending XXXX for Rs 500.00 at CRED_FASTAG on 16-08-2024 14:00:55. Authorization code
        Pattern pattern = Pattern.compile("Thank you for using your HDFC Bank Credit Card ending .... for Rs .* at .* on (.*). Authorization code");
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            String substring = matcher.group(1);
            return LocalDateTime.parse(substring, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        }

        return LocalDateTime.now();
    }
}


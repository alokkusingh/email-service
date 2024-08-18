package com.alok.email.parser.retriver.transactiondate.impl;

import com.alok.email.parser.retriver.transactiondate.TransactionDateRetriever;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class HdfcCCTransactionDateRetriever implements TransactionDateRetriever {

    // Thank you for using your HDFC Bank Credit Card ending XXXX for Rs 500.00 at CRED_FASTAG on 16-08-2024 14:00:55. Authorization code
    private static final Pattern PATTERN = Pattern.compile("Thank you for using your HDFC Bank Credit Card ending .... for Rs .* at .* on (.*). Authorization code");
    @Override
    public LocalDateTime retrieve(String content) {

        Matcher matcher = PATTERN.matcher(content);

        if (matcher.find()) {
            String substring = matcher.group(1);
            return LocalDateTime.parse(substring, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        } else {
            log.warn("Pattern not found in the content.");
        }

        return LocalDateTime.now();
    }
}


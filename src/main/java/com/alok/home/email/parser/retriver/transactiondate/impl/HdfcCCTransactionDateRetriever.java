package com.alok.home.email.parser.retriver.transactiondate.impl;

import com.alok.home.email.parser.retriver.transactiondate.TransactionDateRetriever;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class HdfcCCTransactionDateRetriever implements TransactionDateRetriever {

    // Thank you for using your HDFC Bank Credit Card ending XXXX for Rs 500.00 at CRED_FASTAG on 16-08-2024 14:00:55. Authorization code
    private static final Pattern PATTERN = Pattern.compile("Thank you for using your HDFC Bank Credit Card ending .... for Rs .* at .* on (.*). Authorization code");
    private static final Pattern PATTERN2 = Pattern.compile("Thank you for using HDFC Bank Card ...... for Rs. .* at .* on (.*) Authorization code:- .* Please note that this transaction was conducted without OTP /PIN.");
    private static final Pattern PATTERN3 = Pattern.compile("Rs\\.\\d+\\.\\d+ is debited from your HDFC Bank Credit Card ending \\d+ towards .* on (.. ..., .... at ..:..:..)\\.");

    @Override
    public LocalDateTime retrieve(String content) {

        Matcher matcher = PATTERN.matcher(content);
        if (matcher.find()) {
            String substring = matcher.group(1);
            return LocalDateTime.parse(substring, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        } else {
            log.warn("Pattern1 not found in the content.");
        }

        matcher = PATTERN2.matcher(content);
        if (matcher.find()) {
            String substring = matcher.group(1);
            return LocalDateTime.parse(substring, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        } else {
            log.warn("Pattern2 not found in the content.");
        }

        matcher = PATTERN3.matcher(content);
        if (matcher.find()) {
            String substring = matcher.group(1);
            return LocalDateTime.parse(substring, DateTimeFormatter.ofPattern("dd MMM, yyyy 'at' HH:mm:ss"));
        } else {
            log.warn("Pattern3 not found in the content.");
        }

        return LocalDateTime.now();
    }
}


package com.alok.email.parser.retriver.transactiondate.impl;

import com.alok.email.parser.retriver.transactiondate.TransactionDateRetriever;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class HdfcSBTransactionDateRetriever implements TransactionDateRetriever {

    // Rs.165.00 has been debited from account **XXXX to VPA paytmqr69gdim0mu8@paytm on 18-08-24. Your UPI transaction reference
    private static final Pattern PATTERN = Pattern.compile("Rs..* has been debited from account.* on (.*). Your UPI transaction");
    @Override
    public LocalDateTime retrieve(String content) {

        Matcher matcher = PATTERN.matcher(content);

        if (matcher.find()) {
            String substring = matcher.group(1);
            return LocalDateTime.parse(substring + " 00:00:00", DateTimeFormatter.ofPattern("dd-MM-yy HH:mm:ss"));
        } else {
            log.warn("Pattern not found in the content.");
        }

        return LocalDateTime.now();
    }
}


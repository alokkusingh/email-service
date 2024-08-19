package com.alok.home.email.parser.retriver.transactiondate.impl;

import com.alok.home.email.parser.retriver.transactiondate.TransactionDateRetriever;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class SbiCCTransactionDateRetriever implements TransactionDateRetriever {

    private static final Pattern PATTERN = Pattern.compile("Rs.* on (.*). Trxn. not done by you");
    @Override
    public LocalDateTime retrieve(String content) {

        Matcher matcher = PATTERN.matcher(content);

        if (matcher.find()) {
            String substring = matcher.group(1);
            return LocalDateTime.parse(substring + " 00:00:00", DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss"));
        } else {
            log.warn("Pattern not found in the content.");
        }

        return LocalDateTime.now();
    }
}


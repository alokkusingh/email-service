package com.alok.home.email.parser.retriver.remarks.impl;

import com.alok.home.email.parser.retriver.remarks.RemarksRetriever;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class HdfcSBRemarksRetriever implements RemarksRetriever {

    // Rs.165.00 has been debited from account **XXXX to VPA paytmqr69gdim0mu8@paytm on 18-08-24. Your UPI transaction reference
    private static final Pattern PATTERN = Pattern.compile("Rs.(.*) Your UPI transaction reference");

    @Override
    public String retrieve(String content) {
        Matcher matcher = PATTERN.matcher(content);

        if (matcher.find()) {
            String substring = matcher.group(1);
            return substring;
        } else {
            log.warn("Pattern not found in the content.");
        }

        return "";
    }
}


package com.alok.home.email.parser.retriver.remarks.impl;

import com.alok.home.email.parser.retriver.remarks.RemarksRetriever;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class HdfcSBRemarksRetriever implements RemarksRetriever {

    // Rs.165.00 has been debited from account **XXXX to VPA paytmqr69gdim0mu8@paytm on 18-08-24. Your UPI transaction reference
    private static final Pattern PATTERN1 = Pattern.compile("Rs.(.*) Your UPI transaction reference");
    private static final Pattern PATTERN2 = Pattern.compile("Your (.*) and A/c xxxxxxx.... is credited");
    private static final Pattern PATTERN3 = Pattern.compile("Alert: (.*) from your Account XX.....");

    @Override
    public String retrieve(String content) {
        Matcher matcher = PATTERN1.matcher(content);
        if (matcher.find()) {
            String substring = matcher.group(1);
            return substring;
        } else {
            log.warn("Pattern not found in the content.");
        }

        matcher = PATTERN2.matcher(content);
        if (matcher.find()) {
            String substring = matcher.group(1);
            return substring;
        } else {
            log.warn("Pattern not found in the content.");
        }

        matcher = PATTERN3.matcher(content);
        if (matcher.find()) {
            String substring = matcher.group(1);
            return substring;
        } else {
            log.warn("Pattern not found in the content.");
        }

        return "";
    }
}


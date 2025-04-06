package com.alok.home.email.parser.retriver.remarks.impl;

import com.alok.home.email.parser.retriver.remarks.RemarksRetriever;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class HdfcCCRemarksRetriever implements RemarksRetriever {

    // Thank you for using your HDFC Bank Credit Card ending XXXX for Rs 500.00 at CRED_FASTAG on 16-08-2024 14:00:55. Authorization code
    private static final Pattern PATTERN = Pattern.compile("Thank you for using your HDFC Bank Credit Card ending .... for (.*). Authorization code");
    private static final Pattern PATTERN2 = Pattern.compile("Thank you for using HDFC Bank Card ...... for (.*) Authorization code");

    @Override
    public String retrieve(String content) {
        Matcher matcher = PATTERN.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            log.warn("Pattern1 not found in the content.");
        }

        matcher = PATTERN2.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            log.warn("Pattern2 not found in the content.");
        }

        return "";
    }
}


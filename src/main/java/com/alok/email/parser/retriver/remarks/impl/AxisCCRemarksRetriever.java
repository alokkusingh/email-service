package com.alok.email.parser.retriver.remarks.impl;

import com.alok.email.parser.retriver.remarks.RemarksRetriever;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class AxisCCRemarksRetriever implements RemarksRetriever {


    // Thank you for using your credit card no. XXYYYY for INR 1300 at GOOGLEPLAY on 16-08-2024 09:23:59 IST.
    private static final Pattern PATTERN = Pattern.compile("Thank you for using your credit card no. XX.... for (.*) IST.");

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


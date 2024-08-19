package com.alok.home.email.parser.retriver.remarks.impl;

import com.alok.home.email.parser.retriver.remarks.RemarksRetriever;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class SbiCCRemarksRetriever implements RemarksRetriever {

    // Rs.235.00 spent on your SBI Credit Card ending XXXX at AMAZON PAY INDIA Pvt Ltd on 15/08/24.
    private static final Pattern PATTERN = Pattern.compile("Rs.(.*). Trxn. not done by you");
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


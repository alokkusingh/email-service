package com.alok.home.email.parser.retriver.amount.impl;

import com.alok.home.email.parser.retriver.amount.AmountRetriever;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class SbiCCAmountRetriever implements AmountRetriever {

    // Rs.235.00 spent on your SBI Credit Card ending XXXX at AMAZON PAY INDIA Pvt Ltd on 15/08/24.
    private static final Pattern PATTERN = Pattern.compile("Rs.([0-9]{1,5}[,]{0,1}[0-9]{1,5}[,]{0,1}[0-9]{1,5}.[0-9]{0,2}) spent on your");

    @Override
    public double retrieve(String content) {
        Matcher matcher = PATTERN.matcher(content);

        if (matcher.find()) {
            String substring = matcher.group(1);
            return Double.parseDouble(substring.replace(",", ""));
        } else {
            log.warn("Pattern not found in the content.");
        }

        return 0.0;
    }
}


package com.alok.home.email.parser.retriver.amount.impl;

import com.alok.home.email.parser.retriver.amount.AmountRetriever;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class AxisCCAmountRetriever implements AmountRetriever {
    private static final Pattern PATTERN = Pattern.compile("Thank you for using your credit card no. XX.... for INR (.*) at ");

    @Override
    public double retrieve(String content) {
        Matcher matcher = PATTERN.matcher(content);

        if (matcher.find()) {
            String substring = matcher.group(1);
            try {
                return Double.parseDouble(substring);
            } catch (NumberFormatException e) {
                // Handle the exception, log or return a default value
                return 0.0;
            }
        } else {
            log.warn("Pattern not found in the content.");
        }

        return 0.0;
    }
}


package com.alok.email.parser.retriver.amount.impl;

import com.alok.email.parser.retriver.amount.AmountRetriever;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class HdfcCCAmountRetriever implements AmountRetriever {

    // Thank you for using your HDFC Bank Credit Card ending XXXX for Rs 500.00 at CRED_FASTAG on 16-08-2024 14:00:55. Authorization code
    private static final Pattern PATTERN = Pattern.compile("Thank you for using your HDFC Bank Credit Card ending .... for Rs ([0-9]{1,5}.[0-9]{2}) at ");

    @Override
    public double retrieve(String content) {
        Matcher matcher = PATTERN.matcher(content);

        if (matcher.find()) {
            String substring = matcher.group(1);
            return Double.parseDouble(substring);
        } else {
            log.warn("Pattern not found in the content.");
        }

        return 0.0;
    }
}


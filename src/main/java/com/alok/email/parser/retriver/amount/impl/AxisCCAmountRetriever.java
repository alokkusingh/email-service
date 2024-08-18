package com.alok.email.parser.retriver.amount.impl;

import com.alok.email.parser.retriver.amount.AmountRetriever;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AxisCCAmountRetriever implements AmountRetriever {
    @Override
    public double retrieve(String content) {
        // Thank you for using your credit card no. XXYYYY for INR 1300 at GOOGLEPLAY on 16-08-2024 09:23:59 IST.
        Pattern pattern = Pattern.compile("Thank you for using your credit card no. XX.... for INR (.*) at ");
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            String substring = matcher.group(1);
            return Double.parseDouble(substring);
        }

        return 0.0;
    }
}


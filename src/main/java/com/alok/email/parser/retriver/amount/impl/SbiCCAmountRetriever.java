package com.alok.email.parser.retriver.amount.impl;

import com.alok.email.parser.retriver.amount.AmountRetriever;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SbiCCAmountRetriever implements AmountRetriever {
    @Override
    public double retrieve(String content) {
        // Rs.235.00 spent on your SBI Credit Card ending XXXX at AMAZON PAY INDIA Pvt Ltd on 15/08/24.
        Pattern pattern = Pattern.compile("Rs.([0-9]{1,5}.[0-9]{2}) spent on your");
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            String substring = matcher.group(1);
            return Double.parseDouble(substring);
        }

        return 0.0;
    }
}


package com.alok.email.parser.retriver.amount.impl;

import com.alok.email.parser.retriver.amount.AmountRetriever;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HdfcCCAmountRetriever implements AmountRetriever {
    @Override
    public double retrieve(String content) {
        // Thank you for using your HDFC Bank Credit Card ending XXXX for Rs 500.00 at CRED_FASTAG on 16-08-2024 14:00:55. Authorization code
        Pattern pattern = Pattern.compile("Thank you for using your HDFC Bank Credit Card ending .... for Rs ([0-9]{1,5}.[0-9]{2}) at ");
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            String substring = matcher.group(1);
            return Double.parseDouble(substring);
        }

        return 0.0;
    }
}


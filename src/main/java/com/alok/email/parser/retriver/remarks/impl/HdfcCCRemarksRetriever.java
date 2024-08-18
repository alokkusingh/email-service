package com.alok.email.parser.retriver.remarks.impl;

import com.alok.email.parser.retriver.remarks.RemarksRetriever;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HdfcCCRemarksRetriever implements RemarksRetriever {
    @Override
    public String retrieve(String content) {
        // Thank you for using your HDFC Bank Credit Card ending XXXX for Rs 500.00 at CRED_FASTAG on 16-08-2024 14:00:55. Authorization code
        Pattern pattern = Pattern.compile("Thank you for using your HDFC Bank Credit Card ending .... for (.*). Authorization code");
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            String substring = matcher.group(1);
            return substring;
        }

        return "";
    }
}


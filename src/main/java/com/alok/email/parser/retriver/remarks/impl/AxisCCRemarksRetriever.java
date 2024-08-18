package com.alok.email.parser.retriver.remarks.impl;

import com.alok.email.parser.retriver.remarks.RemarksRetriever;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AxisCCRemarksRetriever implements RemarksRetriever {
    @Override
    public String retrieve(String content) {
        // Thank you for using your credit card no. XXYYYY for INR 1300 at GOOGLEPLAY on 16-08-2024 09:23:59 IST.
        Pattern pattern = Pattern.compile("Thank you for using your credit card no. XX.... for (.*) IST.");
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            String substring = matcher.group(1);
            return substring;
        }

        return "";
    }
}


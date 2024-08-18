package com.alok.email.parser.retriver.remarks.impl;

import com.alok.email.parser.retriver.remarks.RemarksRetriever;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SbiCCRemarksRetriever implements RemarksRetriever {
    @Override
    public String retrieve(String content) {
        // Rs.235.00 spent on your SBI Credit Card ending XXXX at AMAZON PAY INDIA Pvt Ltd on 15/08/24.
        Pattern pattern = Pattern.compile("Rs.(.*). Trxn. not done by you");
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            String substring = matcher.group(1);
            return substring;
        }

        return "";
    }
}


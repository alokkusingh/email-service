package com.alok.email.parser.retriver.remarks.impl;

import com.alok.email.parser.retriver.remarks.RemarksRetriever;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HdfcSBRemarksRetriever implements RemarksRetriever {
    @Override
    public String retrieve(String content) {
        // Rs.165.00 has been debited from account **XXXX to VPA paytmqr69gdim0mu8@paytm on 18-08-24. Your UPI transaction reference
        Pattern pattern = Pattern.compile("Rs.(.*) Your UPI transaction reference");
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            String substring = matcher.group(1);
            return substring;
        }

        return "";
    }
}


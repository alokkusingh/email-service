package com.alok.email.parser.retriver.amount.impl;

import com.alok.email.parser.retriver.amount.AmountRetriever;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HdfcSBAmountRetriever implements AmountRetriever {
    @Override
    public double retrieve(String content) {

        // Rs.165.00 has been debited from account **XXXX to VPA paytmqr69gdim0mu8@paytm on 18-08-24. Your UPI transaction reference
        Pattern pattern = Pattern.compile("Rs.([0-9]{1,5}.[0-9]{2}) has been debited from account");
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            String substring = matcher.group(1);
            return Double.parseDouble(substring);
        }

        return 0.0;
    }
}


package com.alok.home.email.parser.retriver.amount.impl;

import com.alok.home.email.parser.retriver.amount.AmountRetriever;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class HdfcSBAmountRetriever implements AmountRetriever {

    // Rs.165.00 has been debited from account **XXXX to VPA paytmqr69gdim0mu8@paytm on 18-08-24. Your UPI transaction reference
    private static final Pattern PATTERN = Pattern.compile("Rs.([0-9]{1,5}.[0-9]{2}) has been debited from account");

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


package com.alok.home.email.parser.retriver.amount.impl;

import com.alok.home.email.parser.retriver.amount.AmountRetriever;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class HdfcSBAmountRetriever implements AmountRetriever {

    // Rs.165.00 has been debited from account **XXXX to VPA paytmqr69gdim0mu8@paytm on 18-08-24. Your UPI transaction reference
    private static final Pattern PATTERN1 = Pattern.compile("Rs.(.*) has been debited from account");
    private static final Pattern PATTERN2 = Pattern.compile("Your A/c xxxxxxxxxx.... is debited for INR ([0-9]{1,5}[,]{0,1}[0-9]{1,5}[,]{0,1}[0-9]{1,5}.[0-9]{0,2}) on ..-..-.. and A/c xxxxxxx.... is credited");
    private static final Pattern PATTERN3 = Pattern.compile("Alert: Cheque No. ............ of INR ([0-9]{1,5}[,]{0,1}[0-9]{1,5}[,]{0,1}[0-9]{1,5}.[0-9]{0,2}), has been received in clearing on ..-...-.. and debited from your Account XX.....");
    private static final Pattern PATTERN4 = Pattern.compile("Rs.(.*) is debited from your account ending 0531 towards VPA.*");

    @Override
    public double retrieve(String content) {

        Matcher matcher = PATTERN1.matcher(content);
        if (matcher.find()) {
            String substring = matcher.group(1);
            return Double.parseDouble(substring.replace(",", ""));
        } else {
            log.warn("Pattern1 not found in the content.");
        }

        matcher = PATTERN2.matcher(content);
        if (matcher.find()) {
            String substring = matcher.group(1);
            return Double.parseDouble(substring.replace(",", ""));
        } else {
            log.warn("Pattern2 not found in the content.");
        }

        matcher = PATTERN3.matcher(content);
        if (matcher.find()) {
            String substring = matcher.group(1);
            return Double.parseDouble(substring.replace(",", ""));
        } else {
            log.warn("Pattern3 not found in the content.");
        }

        matcher = PATTERN4.matcher(content);
        if (matcher.find()) {
            String substring = matcher.group(1);
            return Double.parseDouble(substring.replace(",", ""));
        } else {
            log.warn("Pattern4 not found in the content.");
        }
        return 0.0;
    }
}


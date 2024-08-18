package com.alok.email.parser.retriver.transactiondate.impl;

import com.alok.email.parser.retriver.transactiondate.TransactionDateRetriever;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HdfcSBTransactionDateRetriever implements TransactionDateRetriever {
    @Override
    public LocalDateTime retrieve(String content) {

        // Rs.165.00 has been debited from account **XXXX to VPA paytmqr69gdim0mu8@paytm on 18-08-24. Your UPI transaction reference
        Pattern pattern = Pattern.compile("Rs..* has been debited from account.* on (.*). Your UPI transaction");
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            String substring = matcher.group(1);
            return LocalDateTime.parse(substring + " 00:00:00", DateTimeFormatter.ofPattern("dd-MM-yy HH:mm:ss"));
        }

        return LocalDateTime.now();
    }
}


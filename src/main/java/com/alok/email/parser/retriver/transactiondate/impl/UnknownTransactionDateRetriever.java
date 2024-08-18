package com.alok.email.parser.retriver.transactiondate.impl;

import com.alok.email.parser.retriver.remarks.RemarksRetriever;
import com.alok.email.parser.retriver.transactiondate.TransactionDateRetriever;

import java.time.LocalDateTime;

public class UnknownTransactionDateRetriever implements TransactionDateRetriever {
    @Override
    public LocalDateTime retrieve(String content) {
        return LocalDateTime.now();
    }
}


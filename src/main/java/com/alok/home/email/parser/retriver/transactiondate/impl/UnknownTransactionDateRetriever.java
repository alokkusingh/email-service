package com.alok.home.email.parser.retriver.transactiondate.impl;

import com.alok.home.email.parser.retriver.transactiondate.TransactionDateRetriever;

import java.time.LocalDateTime;

public class UnknownTransactionDateRetriever implements TransactionDateRetriever {
    @Override
    public LocalDateTime retrieve(String content) {
        return LocalDateTime.now();
    }
}


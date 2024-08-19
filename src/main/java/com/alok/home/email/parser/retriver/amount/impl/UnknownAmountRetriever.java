package com.alok.home.email.parser.retriver.amount.impl;

import com.alok.home.email.parser.retriver.amount.AmountRetriever;

public class UnknownAmountRetriever implements AmountRetriever {
    @Override
    public double retrieve(String content) {
        return 0;
    }
}


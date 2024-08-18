package com.alok.email.parser.retriver.remarks.impl;

import com.alok.email.parser.retriver.amount.AmountRetriever;
import com.alok.email.parser.retriver.remarks.RemarksRetriever;

public class UnknownRemarksRetriever implements RemarksRetriever {
    @Override
    public String retrieve(String content) {
        return "";
    }
}


package com.alok.email.parser.retriver.transactiondate;

import java.time.LocalDateTime;

public interface TransactionDateRetriever {

    LocalDateTime retrieve(String content);
}


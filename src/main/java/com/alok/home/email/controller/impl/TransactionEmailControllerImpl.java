package com.alok.home.email.controller.impl;

import com.alok.home.email.controller.TransactionEmailController;
import com.alok.home.email.parser.dto.TransactionEmailDTO;
import com.alok.home.email.service.TransactionEmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionEmailControllerImpl extends TransactionEmailController {

    private final TransactionEmailService transactionEmailService;

    public TransactionEmailControllerImpl(TransactionEmailService transactionEmailService) {
        this.transactionEmailService = transactionEmailService;
    }

    public ResponseEntity<List<TransactionEmailDTO>> getTransactions(
            Boolean verified
    ) {
        return ResponseEntity.ok()
                .body(transactionEmailService.getTransactions(verified));
    }

    public ResponseEntity<Void> updateTransaction(
            String id,
            TransactionEmailDTO transactionEmailDTO
    ) {

        transactionEmailService.updateTransactionVerified(id, transactionEmailDTO);
        return ResponseEntity.ok().build();
    }
}

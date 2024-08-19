package com.alok.home.email.controller;


import com.alok.home.email.parser.dto.TransactionEmailDTO;
import com.alok.home.email.service.TransactionEmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/transactions")
@RestController
public class TransactionEmailController {

    private TransactionEmailService transactionEmailService;

    public TransactionEmailController(TransactionEmailService transactionEmailService) {
        this.transactionEmailService = transactionEmailService;
    }

    @GetMapping()
    public ResponseEntity<List<TransactionEmailDTO>> getTransactions(
            @RequestParam(value = "verified", defaultValue = "false") Boolean verified
    ) {
        return ResponseEntity.ok()
                .body(transactionEmailService.getTransactions(verified));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTransaction(
            @PathVariable(value = "id") String id,
            @RequestParam(value = "accepted", defaultValue = "true") Boolean accepted
    ) {

        transactionEmailService.updateTransactionVerified(id, accepted);
        return ResponseEntity.ok().build();
    }
}

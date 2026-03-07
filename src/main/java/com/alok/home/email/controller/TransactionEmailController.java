package com.alok.home.email.controller;

import com.alok.home.email.parser.dto.TransactionEmailDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/transactions")
@RestController
public abstract class TransactionEmailController {

    @GetMapping()
    public abstract ResponseEntity<List<TransactionEmailDTO>> getTransactions(
            @RequestParam(value = "verified", defaultValue = "false") Boolean verified
    );

    @PutMapping("/{id}")
    public abstract ResponseEntity<Void> updateTransaction(
            @PathVariable(value = "id") String id,
            @RequestBody TransactionEmailDTO transactionEmailDTO
    );
}

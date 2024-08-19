package com.alok.home.email.service;

import com.alok.home.email.entity.impl.TransactionEmail;
import com.alok.home.email.parser.dto.TransactionEmailDTO;
import com.alok.home.email.repository.TransactionEmailRepository;
import com.alok.home.commons.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TransactionEmailService {

    private TransactionEmailRepository transactionEmailRepository;

    public TransactionEmailService(TransactionEmailRepository transactionEmailRepository) {
        this.transactionEmailRepository = transactionEmailRepository;
    }

    public List<TransactionEmailDTO> getTransactions(Boolean verified) {
        return transactionEmailRepository.findTransactionsByVerified(verified).stream()
                .map(
                        transactionEmail -> TransactionEmailDTO.builder()
                                .id(transactionEmail.getId())
                                .bank(transactionEmail.getBank())
                                .description(transactionEmail.getDescription())
                                .amount(transactionEmail.getAmount())
                                .build()
                )
                .toList();
    }

    @Transactional
    public void updateTransactionVerified(String id, Boolean accepted) {

        TransactionEmail transaction = transactionEmailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction ID not found"));

        transaction.setVerified(true);
        transaction.setAccepted(accepted);
    }
}

package com.alok.home.email.service;

import com.alok.home.email.entity.impl.TransactionEmail;
import com.alok.home.email.parser.dto.TransactionEmailDTO;
import com.alok.home.email.repository.TransactionEmailRepository;
import com.alok.home.commons.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
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
                                .timestamp(transactionEmail.getTimestamp())
                                .verified(transactionEmail.isVerified())
                                .verifiedBy(transactionEmail.getVerifiedBy())
                                .accepted(transactionEmail.isAccepted())
                                .build()
                )
                .sorted(Comparator.comparing(TransactionEmailDTO::getTimestamp).reversed())
                .toList();
    }

    @Transactional
    public void updateTransactionVerified(String id, TransactionEmailDTO transactionEmailDTO) {

        TransactionEmail transaction = transactionEmailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction ID not found"));

        transaction.setVerified(transactionEmailDTO.isVerified());
        transaction.setAccepted(transactionEmailDTO.isAccepted());
        transaction.setVerifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}

package com.alok.home.email.service;

import com.alok.home.commons.dto.exception.ResourceNotFoundException;
import com.alok.home.email.entity.impl.TransactionEmail;
import com.alok.home.email.parser.dto.TransactionEmailDTO;
import com.alok.home.email.repository.TransactionEmailRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class TransactionEmailService {

    private final TransactionEmailRepository transactionEmailRepository;

    public TransactionEmailService(TransactionEmailRepository transactionEmailRepository) {
        this.transactionEmailRepository = transactionEmailRepository;
    }

    public List<TransactionEmailDTO> getTransactions(Boolean verified) {
        return transactionEmailRepository.findTransactionsByVerified(verified).stream()
                .map(
                        transactionEmail -> new TransactionEmailDTO(
                                transactionEmail.getId(),
                                transactionEmail.getBank(),
                                transactionEmail.getDescription(),
                                transactionEmail.getAmount(),
                                transactionEmail.isVerified(),
                                transactionEmail.getVerifiedBy(),
                                transactionEmail.isAccepted(),
                                transactionEmail.getTimestamp()
                        )
                )
                .sorted(Comparator.comparing(TransactionEmailDTO::timestamp).reversed())
                .toList();
    }

    @Transactional
    public void updateTransactionVerified(String id, TransactionEmailDTO transactionEmailDTO) {

        TransactionEmail transaction = transactionEmailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction ID not found"));

        transaction.setVerified(transactionEmailDTO.verified());
        transaction.setAccepted(transactionEmailDTO.accepted());
        transaction.setVerifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}

package com.alok.home.email.repository;

import com.alok.home.email.entity.impl.TransactionEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionEmailRepository  extends JpaRepository<TransactionEmail, String> {

    @Query("select t FROM TransactionEmail t WHERE t.verified = ?1")
    List<TransactionEmail> findTransactionsByVerified(Boolean verified);
}

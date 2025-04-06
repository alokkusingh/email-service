package com.alok.home.email.repository;

import com.alok.home.email.entity.impl.FailedEmail;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FailedEmailRepository extends JpaRepository<FailedEmail, String> {

    @Query("update FailedEmail e set e.processed = ?2, e.success = ?3 where e.id = ?1")
    @Modifying
    @Transactional
    void updateProcessedById(String id, boolean processed, boolean success);
}

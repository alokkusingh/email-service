package com.alok.home.email.repository;

import com.alok.home.email.entity.impl.FailedEmail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FailedEmailRepository extends JpaRepository<FailedEmail, String> {
}

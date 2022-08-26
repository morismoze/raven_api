package com.raven.api.repository;

import com.raven.api.model.PasswordResetToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByUuidCode(String uuid);

    Optional<PasswordResetToken> findByUser_Id(Long userId);

    void deleteByUser_Id(Long userId);
}


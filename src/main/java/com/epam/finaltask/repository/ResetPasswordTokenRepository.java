package com.epam.finaltask.repository;

import com.epam.finaltask.model.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {
    @Query("SELECT t FROM ResetPasswordToken t LEFT JOIN FETCH t.user WHERE t.token = :token")
    Optional<ResetPasswordToken> findByToken(String token);
}

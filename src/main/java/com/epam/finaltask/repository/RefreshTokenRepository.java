package com.epam.finaltask.repository;

import com.epam.finaltask.model.RefreshToken;
import com.epam.finaltask.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    boolean existsByUser(User user);

    void deleteByUser(User user);
}

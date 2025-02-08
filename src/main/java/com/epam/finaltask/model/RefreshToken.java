package com.epam.finaltask.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_token")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}

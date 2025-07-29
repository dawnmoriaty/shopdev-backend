package com.example.shopdev.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String token;

    private Instant expiryDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}

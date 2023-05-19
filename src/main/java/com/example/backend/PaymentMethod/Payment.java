package com.example.backend.PaymentMethod;

import com.example.backend.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    @Column(nullable = false)
    private String paymentType;
    @Column(nullable = false)
    private String provider;
    @Column(nullable = false)
    private String cardHolderName;
    @Column(nullable = false)
    private String cardNumber;
    @Column(nullable = false)
    private String expirationDate;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
}
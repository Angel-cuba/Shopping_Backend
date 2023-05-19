package com.example.backend.WishesList;

import com.example.backend.User.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "wishes")
public class Wishes {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    @Column
    private List<String> userWishes;
    @Column
    private Integer totalOfItems;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
}

package com.example.backend.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    @Column(unique = true, nullable = false, columnDefinition = "varchar(20)")
    private String username;
    @Column(nullable = false, columnDefinition = "varchar(20)")
    private String firstname;
    @Column(nullable = false, columnDefinition = "varchar(20)")
    private String lastname;
    @Column(nullable = false, columnDefinition = "varchar(12)")
    private String phone;
    @Column(unique = true, nullable = false, columnDefinition = "varchar(40)")
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, columnDefinition = "varchar(50)")
    private String password;
    @Column(nullable = false, columnDefinition = "varchar(10)")
    private String role;

}
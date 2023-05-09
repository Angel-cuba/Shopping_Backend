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
  @Column(nullable = false)
  private String username;
  @Column(nullable = false)
  private String firstname;
  @Column(nullable = false)
  private String lastname;
  @Column(unique = true)
  private String phone;
  @Column(unique = true)
  private String email;
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Column(nullable = false)
  private String password;
  @Enumerated(EnumType.STRING)
  private Role role;

}
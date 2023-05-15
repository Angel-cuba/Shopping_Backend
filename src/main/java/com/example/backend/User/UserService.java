package com.example.backend.User;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createOne(User user) {
        return userRepository.save(user);
    }

    public User updateOne(User user) {
        User userToUpdate = userRepository.findById(user.getId()).orElse(null);
        if (userToUpdate == null) {
            return null;
        }
        userToUpdate.setFirstname(user.getFirstname());
        userToUpdate.setLastname(user.getLastname());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setPhone(user.getPhone());
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
        userToUpdate.setRole(user.getRole());
        return userRepository.save(userToUpdate);
    }

    public void deleteById(UUID id) {
        User userToDelete = userRepository.findById(id).orElse(null);
        if (userToDelete == null) {
            return;
        }
        userRepository.deleteById(id);
    }

    public User findUserByEmail(String email) {
         return userRepository.findUserByEmail(email);
    }

    public User findUserName(String username) {
         return userRepository.findByUsername(username);
    }
}


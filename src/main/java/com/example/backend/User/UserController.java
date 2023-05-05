package com.example.backend.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable UUID id) {
        return userService.findById(id);
    }

    @PostMapping
    public User createOne(@RequestBody User user) {
        return userService.createOne(user);
    }

    @PutMapping
    public User updateOne(@RequestBody User user) {
        return userService.updateOne(user);
    }

    @DeleteMapping("/{id}")
    public void deleteOne(@PathVariable UUID id) {
        userService.deleteById(id);
    }
}

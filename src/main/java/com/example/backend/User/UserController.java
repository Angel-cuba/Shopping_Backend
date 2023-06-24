package com.example.backend.User;

import com.example.backend.Authentication.AuthRequest;
import com.example.backend.Utils.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:3000", "https://starlit-bienenstitch-282c7d.netlify.app"})
@RestController
@RequestMapping("api/v1/users")
public class UserController {

  @Autowired
  private UserService userService;
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private JwtHelper jwtHelper;

  @GetMapping()
  public List<User> findAll() {
    return userService.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> findById(@PathVariable UUID id) {
    User user = userService.findById(id);
    if (user == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @PostMapping("/signup")
  public ResponseEntity<String> signup(@RequestBody User user) {
    User existingUser = userService.findUserByEmail(user.getEmail());
    if (existingUser != null) {
      return ResponseEntity.badRequest().body("Email already exists");
    }
    existingUser = userService.findUserName(user.getUsername());
    if (existingUser != null) {
      return ResponseEntity.badRequest().body("Username already exists");
    }
    user.setUsername(user.getUsername());
    user.setFirstname(user.getFirstname());
    user.setLastname(user.getLastname());
    user.setPhone(user.getPhone());
    user.setEmail(user.getEmail());
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRole(Role.USER);
    userService.createOne(user);
    String token = jwtHelper.generateToken(user);
    return ResponseEntity.ok(token);
  }

  @PostMapping("/signin")
  public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {
    try {
      authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                      authRequest.getUsername(),
                      authRequest.getPassword()
              )
      );
    } catch (AuthenticationException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }

    User user = userService.findUserName(authRequest.getUsername());
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
    }

    String token = jwtHelper.generateToken(user);
    return ResponseEntity.ok(token);
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

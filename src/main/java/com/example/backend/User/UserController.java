package com.example.backend.User;

import com.example.backend.Authentication.AuthRequest;
import com.example.backend.Utils.JwtHelper;
import com.example.backend.Utils.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "https://starlit-bienenstitch-282c7d.netlify.app"})
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService           userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder       passwordEncoder;
    private final JwtHelper             jwtHelper;

    public UserController(UserService userService,
                          AuthenticationManager authenticationManager,
                          PasswordEncoder passwordEncoder,
                          JwtHelper jwtHelper) {
        this.userService           = userService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder       = passwordEncoder;
        this.jwtHelper             = jwtHelper;
    }

    @GetMapping()
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable UUID id) {
        User user = userService.findById(id);
        if (user == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        if (userService.findUserByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        if (userService.findUserName(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userService.createOne(user);
        return ResponseEntity.ok(jwtHelper.generateToken(user));
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
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        return ResponseEntity.ok(jwtHelper.generateToken(user));
    }

    @PutMapping
    public ResponseEntity<User> updateOne(@RequestBody User user) {
        User authenticatedUser = resolveAuthenticatedUser();
        boolean isAdmin = authenticatedUser != null && authenticatedUser.getRole() == Role.ADMIN;
        boolean isOwner = authenticatedUser != null && authenticatedUser.getId().equals(user.getId());
        if (!isAdmin && !isOwner) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        if (!isAdmin) user.setRole(authenticatedUser.getRole());
        return ResponseEntity.ok(userService.updateOne(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOne(@PathVariable UUID id) {
        User authenticatedUser = resolveAuthenticatedUser();
        boolean isAdmin = authenticatedUser != null && authenticatedUser.getRole() == Role.ADMIN;
        boolean isOwner = authenticatedUser != null && authenticatedUser.getId().equals(id);
        if (!isAdmin && !isOwner) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private User resolveAuthenticatedUser() {
        String username = SecurityUtils.getAuthenticatedUsername();
        if (username == null) return null;
        return userService.findUserName(username);
    }
}

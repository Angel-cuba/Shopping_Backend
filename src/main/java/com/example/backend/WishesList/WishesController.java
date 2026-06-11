package com.example.backend.WishesList;

import com.example.backend.User.Role;
import com.example.backend.User.User;
import com.example.backend.User.UserService;
import com.example.backend.Utils.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "https://starlit-bienenstitch-282c7d.netlify.app"})
@RestController
@RequestMapping("/api/v1/wishes")
public class WishesController {

    private final WishesService wishesService;
    private final UserService userService;

    public WishesController(WishesService wishesService, UserService userService) {
        this.wishesService = wishesService;
        this.userService   = userService;
    }

    @GetMapping
    public Iterable<Wishes> list() {
        return wishesService.listAll();
    }

    @GetMapping("/{id}")
    public Wishes get(@PathVariable UUID id) {
        return wishesService.get(id);
    }

    /**
     * Returns wishlist items for the given userId.
     * Security: only the account owner or an ADMIN can read a user's wishlist.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Wishes>> getWishesByUserId(@PathVariable UUID userId) {
        User authenticatedUser = resolveAuthenticatedUser();
        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        boolean isAdmin = authenticatedUser.getRole() == Role.ADMIN;
        boolean isOwner = authenticatedUser.getId().equals(userId);
        if (!isAdmin && !isOwner) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(wishesService.getWishesByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<Wishes> save(@RequestBody Wishes wishes) {
        User authenticatedUser = resolveAuthenticatedUser();
        if (authenticatedUser == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (wishes.getUser() == null || wishes.getUser().getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        boolean isAdmin = authenticatedUser.getRole() == Role.ADMIN;
        boolean isOwner = authenticatedUser.getId().equals(wishes.getUser().getId());
        if (!isAdmin && !isOwner) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(wishesService.createWishes(wishes));
    }

    @PutMapping
    public ResponseEntity<Wishes> update(@RequestBody Wishes wishes) {
        User authenticatedUser = resolveAuthenticatedUser();
        if (authenticatedUser == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (wishes.getId() == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        // Fetch the persisted record — never trust request body for ownership
        Wishes existing = wishesService.get(wishes.getId());
        if (existing == null) return ResponseEntity.notFound().build();
        boolean isAdmin = authenticatedUser.getRole() == Role.ADMIN;
        boolean isOwner = authenticatedUser.getId().equals(existing.getUser().getId());
        if (!isAdmin && !isOwner) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        Wishes updated = wishesService.updateWishes(wishes);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        User authenticatedUser = resolveAuthenticatedUser();
        if (authenticatedUser == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        Wishes existing = wishesService.get(id);
        if (existing == null) return ResponseEntity.notFound().build();
        boolean isAdmin = authenticatedUser.getRole() == Role.ADMIN;
        boolean isOwner = authenticatedUser.getId().equals(existing.getUser().getId());
        if (!isAdmin && !isOwner) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        wishesService.deleteWishListById(id);
        return ResponseEntity.noContent().build();
    }

    private User resolveAuthenticatedUser() {
        String username = SecurityUtils.getAuthenticatedUsername();
        if (username == null) return null;
        return userService.findUserName(username);
    }
}

package com.example.backend.WishesList;

import com.example.backend.Exceptions.GlobalExceptionHandler;
import com.example.backend.User.Role;
import com.example.backend.User.User;
import com.example.backend.User.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Controller-layer tests for WishesController ownership guards (Sprint A security fix).
 * Uses standalone MockMvc — no Spring context overhead.
 *
 * Ownership rules:
 *   POST  /api/v1/wishes        — only the account owner or ADMIN can create
 *   PUT   /api/v1/wishes        — only the account owner or ADMIN can update
 *   DELETE /api/v1/wishes/{id}  — only the account owner or ADMIN can delete
 */
class WishesControllerTest {

    private MockMvc mockMvc;
    private WishesService wishesService;
    private UserService   userService;
    private ObjectMapper  objectMapper;

    @BeforeEach
    void setUp() {
        wishesService = mock(WishesService.class);
        userService   = mock(UserService.class);
        objectMapper  = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        mockMvc = MockMvcBuilders
            .standaloneSetup(new WishesController(wishesService, userService))
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();

        SecurityContext ctx = SecurityContextHolder.createEmptyContext();
        ctx.setAuthentication(new UsernamePasswordAuthenticationToken("testuser", null, List.of()));
        SecurityContextHolder.setContext(ctx);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    // ── Helpers ─────────────────────────────────────────────────────────────────

    private User buildUser(UUID id, Role role) {
        User u = new User();
        u.setId(id);
        u.setUsername("testuser");
        u.setRole(role);
        return u;
    }

    private Wishes buildWishes(UUID wishId, UUID ownerId) {
        User owner = buildUser(ownerId, Role.USER);
        Wishes w = new Wishes();
        w.setId(wishId);
        w.setUserWishes(List.of("prod-1", "prod-2"));
        w.setTotalOfItems(2);
        w.setUser(owner);
        return w;
    }

    /** Minimal JSON body with user.id set to the given userId. */
    private String wishesJson(UUID wishId, UUID userId) throws Exception {
        return String.format(
            "{\"id\":\"%s\",\"userWishes\":[\"prod-1\"],\"totalOfItems\":1,\"user\":{\"id\":\"%s\"}}",
            wishId != null ? wishId : UUID.randomUUID(),
            userId
        );
    }

    // ── POST /api/v1/wishes ──────────────────────────────────────────────────────

    @Test
    void post_owner_returns201() throws Exception {
        UUID userId  = UUID.randomUUID();
        User owner   = buildUser(userId, Role.USER);
        Wishes saved = buildWishes(UUID.randomUUID(), userId);

        when(userService.findUserName("testuser")).thenReturn(owner);
        when(wishesService.createWishes(any())).thenReturn(saved);

        mockMvc.perform(post("/api/v1/wishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(wishesJson(null, userId)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.totalOfItems").value(2));
    }

    @Test
    void post_differentUser_returns403() throws Exception {
        UUID authenticatedId = UUID.randomUUID();
        UUID victimId        = UUID.randomUUID();
        User user = buildUser(authenticatedId, Role.USER);

        when(userService.findUserName("testuser")).thenReturn(user);

        mockMvc.perform(post("/api/v1/wishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(wishesJson(null, victimId)))
            .andExpect(status().isForbidden());

        verify(wishesService, never()).createWishes(any());
    }

    @Test
    void post_admin_canCreateForAnyUser_returns201() throws Exception {
        UUID adminId  = UUID.randomUUID();
        UUID victimId = UUID.randomUUID();
        User admin    = buildUser(adminId, Role.ADMIN);
        Wishes saved  = buildWishes(UUID.randomUUID(), victimId);

        when(userService.findUserName("testuser")).thenReturn(admin);
        when(wishesService.createWishes(any())).thenReturn(saved);

        mockMvc.perform(post("/api/v1/wishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(wishesJson(null, victimId)))
            .andExpect(status().isCreated());
    }

    @Test
    void post_missingUser_returns400() throws Exception {
        UUID authenticatedId = UUID.randomUUID();
        User user = buildUser(authenticatedId, Role.USER);

        when(userService.findUserName("testuser")).thenReturn(user);

        mockMvc.perform(post("/api/v1/wishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userWishes\":[\"prod-1\"],\"totalOfItems\":1}"))
            .andExpect(status().isBadRequest());
    }

    // ── PUT /api/v1/wishes ───────────────────────────────────────────────────────

    @Test
    void put_owner_returns200() throws Exception {
        UUID userId   = UUID.randomUUID();
        UUID wishId   = UUID.randomUUID();
        User owner    = buildUser(userId, Role.USER);
        Wishes updated = buildWishes(wishId, userId);

        when(userService.findUserName("testuser")).thenReturn(owner);
        when(wishesService.updateWishes(any())).thenReturn(updated);

        mockMvc.perform(put("/api/v1/wishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(wishesJson(wishId, userId)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalOfItems").value(2));
    }

    @Test
    void put_differentUser_returns403() throws Exception {
        UUID authenticatedId = UUID.randomUUID();
        UUID victimId        = UUID.randomUUID();
        User user = buildUser(authenticatedId, Role.USER);

        when(userService.findUserName("testuser")).thenReturn(user);

        mockMvc.perform(put("/api/v1/wishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(wishesJson(UUID.randomUUID(), victimId)))
            .andExpect(status().isForbidden());

        verify(wishesService, never()).updateWishes(any());
    }

    @Test
    void put_admin_canUpdateAnyUser_returns200() throws Exception {
        UUID adminId  = UUID.randomUUID();
        UUID victimId = UUID.randomUUID();
        UUID wishId   = UUID.randomUUID();
        User admin    = buildUser(adminId, Role.ADMIN);
        Wishes updated = buildWishes(wishId, victimId);

        when(userService.findUserName("testuser")).thenReturn(admin);
        when(wishesService.updateWishes(any())).thenReturn(updated);

        mockMvc.perform(put("/api/v1/wishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(wishesJson(wishId, victimId)))
            .andExpect(status().isOk());
    }

    @Test
    void put_serviceReturnsNull_returns404() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID wishId = UUID.randomUUID();
        User owner  = buildUser(userId, Role.USER);

        when(userService.findUserName("testuser")).thenReturn(owner);
        when(wishesService.updateWishes(any())).thenReturn(null);

        mockMvc.perform(put("/api/v1/wishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(wishesJson(wishId, userId)))
            .andExpect(status().isNotFound());
    }

    // ── DELETE /api/v1/wishes/{id} ───────────────────────────────────────────────

    @Test
    void delete_owner_returns204() throws Exception {
        UUID userId  = UUID.randomUUID();
        UUID wishId  = UUID.randomUUID();
        User owner   = buildUser(userId, Role.USER);
        Wishes found = buildWishes(wishId, userId);

        when(userService.findUserName("testuser")).thenReturn(owner);
        when(wishesService.get(wishId)).thenReturn(found);
        doNothing().when(wishesService).deleteWishListById(wishId);

        mockMvc.perform(delete("/api/v1/wishes/" + wishId))
            .andExpect(status().isNoContent());

        verify(wishesService).deleteWishListById(wishId);
    }

    @Test
    void delete_differentUser_returns403() throws Exception {
        UUID authenticatedId = UUID.randomUUID();
        UUID ownerId         = UUID.randomUUID();
        UUID wishId          = UUID.randomUUID();
        User user    = buildUser(authenticatedId, Role.USER);
        Wishes found = buildWishes(wishId, ownerId);

        when(userService.findUserName("testuser")).thenReturn(user);
        when(wishesService.get(wishId)).thenReturn(found);

        mockMvc.perform(delete("/api/v1/wishes/" + wishId))
            .andExpect(status().isForbidden());

        verify(wishesService, never()).deleteWishListById(any());
    }

    @Test
    void delete_admin_canDeleteAnyUser_returns204() throws Exception {
        UUID adminId  = UUID.randomUUID();
        UUID ownerId  = UUID.randomUUID();
        UUID wishId   = UUID.randomUUID();
        User admin    = buildUser(adminId, Role.ADMIN);
        Wishes found  = buildWishes(wishId, ownerId);

        when(userService.findUserName("testuser")).thenReturn(admin);
        when(wishesService.get(wishId)).thenReturn(found);
        doNothing().when(wishesService).deleteWishListById(wishId);

        mockMvc.perform(delete("/api/v1/wishes/" + wishId))
            .andExpect(status().isNoContent());
    }

    @Test
    void delete_wishNotFound_returns404() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID wishId = UUID.randomUUID();
        User user   = buildUser(userId, Role.USER);

        when(userService.findUserName("testuser")).thenReturn(user);
        when(wishesService.get(wishId)).thenReturn(null);

        mockMvc.perform(delete("/api/v1/wishes/" + wishId))
            .andExpect(status().isNotFound());

        verify(wishesService, never()).deleteWishListById(any());
    }

    // ── Deleted-user / unauthenticated paths ─────────────────────────────────────

    @Test
    void post_deletedUser_returns401() throws Exception {
        when(userService.findUserName("testuser")).thenReturn(null);

        mockMvc.perform(post("/api/v1/wishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(wishesJson(null, UUID.randomUUID())))
            .andExpect(status().isUnauthorized());

        verify(wishesService, never()).createWishes(any());
    }

    @Test
    void post_noAuthentication_returns401() throws Exception {
        SecurityContextHolder.clearContext();

        mockMvc.perform(post("/api/v1/wishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(wishesJson(null, UUID.randomUUID())))
            .andExpect(status().isUnauthorized());

        verify(userService, never()).findUserName(any());
        verify(wishesService, never()).createWishes(any());
    }

    // ── GET /api/v1/wishes/user/{userId} — owner guard ──────────────────────────

    @Test
    void getByUser_owner_returns200() throws Exception {
        UUID userId = UUID.randomUUID();
        User owner  = buildUser(userId, Role.USER);

        when(userService.findUserName("testuser")).thenReturn(owner);
        when(wishesService.getWishesByUserId(userId)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/wishes/user/" + userId))
            .andExpect(status().isOk());
    }

    @Test
    void getByUser_differentUser_returns403() throws Exception {
        UUID authenticatedId = UUID.randomUUID();
        UUID requestedUserId = UUID.randomUUID();
        User user = buildUser(authenticatedId, Role.USER);

        when(userService.findUserName("testuser")).thenReturn(user);

        mockMvc.perform(get("/api/v1/wishes/user/" + requestedUserId))
            .andExpect(status().isForbidden());
    }

    @Test
    void getByUser_admin_returns200ForAnyUser() throws Exception {
        UUID adminId         = UUID.randomUUID();
        UUID requestedUserId = UUID.randomUUID();
        User admin = buildUser(adminId, Role.ADMIN);

        when(userService.findUserName("testuser")).thenReturn(admin);
        when(wishesService.getWishesByUserId(requestedUserId)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/wishes/user/" + requestedUserId))
            .andExpect(status().isOk());
    }
}

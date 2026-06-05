package com.example.backend.Orders;

import com.example.backend.Exceptions.GlobalExceptionHandler;
import com.example.backend.Exceptions.NotFoundException;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Controller-layer tests using standalone MockMvc (no Spring context overhead).
 * GlobalExceptionHandler is registered so exception-to-HTTP-status mappings are tested end-to-end.
 *
 * Security rules (hasRole ADMIN) are enforced by SecurityConfig requestMatchers and cannot
 * be tested without a full security filter chain — they are documented in SecurityConfig.java.
 *
 * Role-based access control (USER gets 403, unauthenticated gets 403) is enforced by:
 *   GET  /api/v1/orders              => hasRole("ADMIN")
 *   PUT  /api/v1/orders/{id}/status  => hasRole("ADMIN")
 *   POST /api/v1/orders              => hasRole("ADMIN")  (deprecated legacy endpoint)
 */
class OrderControllerTest {

    private MockMvc mockMvc;
    private OrderService orderService;
    private UserService userService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        orderService = mock(OrderService.class);
        userService  = mock(UserService.class);
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // registers JavaTimeModule for LocalDateTime

        mockMvc = MockMvcBuilders
            .standaloneSetup(new OrderController(orderService, userService))
            .setControllerAdvice(new GlobalExceptionHandler()) // maps domain exceptions → HTTP status
            .build();

        // Default security context: authenticated as "testuser"
        SecurityContext ctx = SecurityContextHolder.createEmptyContext();
        ctx.setAuthentication(new UsernamePasswordAuthenticationToken("testuser", null, List.of()));
        SecurityContextHolder.setContext(ctx);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    private User buildUser(UUID id, Role role) {
        User u = new User();
        u.setId(id);
        u.setUsername("testuser");
        u.setRole(role);
        return u;
    }

    private AdminOrderDTO buildDTO(UUID orderId, UUID userId) {
        return new AdminOrderDTO(
            orderId, List.of("item-1"), "CARD", "123 Main St",
            "STANDARD", 500, 10500, OrderStatus.PENDING, LocalDateTime.now(),
            "Alice", "Smith", "alice@example.com", userId
        );
    }

    // -----------------------------------------------------------------------
    // GET /api/v1/orders  — getAllOrders
    // -----------------------------------------------------------------------

    @Test
    void getAllOrders_returnsListFromService() throws Exception {
        UUID orderId = UUID.randomUUID();
        UUID userId  = UUID.randomUUID();
        AdminOrderDTO dto = new AdminOrderDTO(
            orderId, List.of("item-1"), "CARD", "123 Main St",
            "STANDARD", 500, 10500, OrderStatus.PENDING, LocalDateTime.now(),
            "Alice", "Smith", "alice@example.com", userId
        );

        when(orderService.findAllOrders()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/orders"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].userFirstname").value("Alice"))
            .andExpect(jsonPath("$[0].userLastname").value("Smith"))
            .andExpect(jsonPath("$[0].status").value("PENDING"))
            .andExpect(jsonPath("$[0].total").value(10500));
    }

    @Test
    void getAllOrders_emptyList_returns200WithEmptyArray() throws Exception {
        when(orderService.findAllOrders()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/orders"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    // -----------------------------------------------------------------------
    // GET /api/v1/orders/{userId}  — getOrdersByUserId (ownership guard)
    // -----------------------------------------------------------------------

    @Test
    void getOrdersByUserId_owner_returns200() throws Exception {
        UUID userId = UUID.randomUUID();
        User owner  = buildUser(userId, Role.USER);
        when(userService.findUserName("testuser")).thenReturn(owner);
        when(orderService.getOrdersByUserId(userId)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/orders/" + userId))
            .andExpect(status().isOk());
    }

    @Test
    void getOrdersByUserId_admin_returns200ForAnyUser() throws Exception {
        UUID ownerId  = UUID.randomUUID();
        UUID adminId  = UUID.randomUUID();
        User admin    = buildUser(adminId, Role.ADMIN);
        when(userService.findUserName("testuser")).thenReturn(admin);
        when(orderService.getOrdersByUserId(ownerId)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/orders/" + ownerId))
            .andExpect(status().isOk());
    }

    @Test
    void getOrdersByUserId_differentUser_returns403() throws Exception {
        UUID requestedUserId = UUID.randomUUID();
        UUID authenticatedId = UUID.randomUUID(); // different from requestedUserId
        User user            = buildUser(authenticatedId, Role.USER);
        when(userService.findUserName("testuser")).thenReturn(user);

        mockMvc.perform(get("/api/v1/orders/" + requestedUserId))
            .andExpect(status().isForbidden());
    }

    // -----------------------------------------------------------------------
    // PUT /api/v1/orders/{id}/status — updateStatus
    // -----------------------------------------------------------------------

    @Test
    void updateStatus_existingOrder_returns200WithUpdatedDTO() throws Exception {
        UUID orderId = UUID.randomUUID();
        UUID userId  = UUID.randomUUID();
        AdminOrderDTO updated = new AdminOrderDTO(
            orderId, List.of("item-1"), "CARD", "123 Main St",
            "STANDARD", 500, 10500, OrderStatus.CONFIRMED, LocalDateTime.now(),
            "Bob", "Jones", "bob@example.com", userId
        );
        StatusUpdateRequest req = new StatusUpdateRequest(OrderStatus.CONFIRMED);

        when(orderService.updateOrderStatus(orderId, OrderStatus.CONFIRMED)).thenReturn(updated);

        mockMvc.perform(put("/api/v1/orders/" + orderId + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("CONFIRMED"))
            .andExpect(jsonPath("$.userFirstname").value("Bob"));
    }

    @Test
    void updateStatus_orderNotFound_returns404() throws Exception {
        UUID orderId = UUID.randomUUID();
        StatusUpdateRequest req = new StatusUpdateRequest(OrderStatus.SHIPPED);

        when(orderService.updateOrderStatus(orderId, OrderStatus.SHIPPED))
            .thenThrow(new NotFoundException("Order not found: " + orderId));

        mockMvc.perform(put("/api/v1/orders/" + orderId + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isNotFound());
    }

    // -----------------------------------------------------------------------
    // POST /api/v1/orders/place — placeOrder (ownership guard + service delegation)
    // -----------------------------------------------------------------------

    @Test
    void placeOrder_happyPath_returns201() throws Exception {
        UUID userId = UUID.randomUUID();
        User owner  = buildUser(userId, Role.USER);
        when(userService.findUserName("testuser")).thenReturn(owner);

        PlaceOrderRequest.OrderItemRequest item = new PlaceOrderRequest.OrderItemRequest(
            UUID.randomUUID(), "Crimson", "img.png", "9", 9900, 1);
        PlaceOrderRequest req = new PlaceOrderRequest(
            userId, List.of(item), "Visa", "1 Main St", "DOOR", 299, 10199);

        AdminOrderDTO dto = buildDTO(UUID.randomUUID(), userId);
        when(orderService.placeOrder(any(PlaceOrderRequest.class))).thenReturn(dto);

        mockMvc.perform(post("/api/v1/orders/place")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void placeOrder_differentUserId_returns403() throws Exception {
        UUID authenticatedId = UUID.randomUUID();
        UUID requestedId     = UUID.randomUUID(); // different — IDOR attempt
        User owner           = buildUser(authenticatedId, Role.USER);
        when(userService.findUserName("testuser")).thenReturn(owner);

        PlaceOrderRequest.OrderItemRequest item = new PlaceOrderRequest.OrderItemRequest(
            UUID.randomUUID(), "Blue", "img.png", "10", 8000, 1);
        PlaceOrderRequest req = new PlaceOrderRequest(
            requestedId, List.of(item), "Mastercard", "2 Oak Ave", "DOOR", 0, 8000);

        mockMvc.perform(post("/api/v1/orders/place")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isForbidden());

        verify(orderService, never()).placeOrder(any());
    }

    @Test
    void placeOrder_productNotFound_returns404() throws Exception {
        UUID userId = UUID.randomUUID();
        User owner  = buildUser(userId, Role.USER);
        when(userService.findUserName("testuser")).thenReturn(owner);
        when(orderService.placeOrder(any(PlaceOrderRequest.class)))
            .thenThrow(new NotFoundException("Product not found"));

        PlaceOrderRequest.OrderItemRequest item = new PlaceOrderRequest.OrderItemRequest(
            UUID.randomUUID(), "Red", "img.png", "8", 7000, 1);
        PlaceOrderRequest req = new PlaceOrderRequest(
            userId, List.of(item), "Visa", "3 Elm St", "DOOR", 299, 7299);

        mockMvc.perform(post("/api/v1/orders/place")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isNotFound());
    }
}

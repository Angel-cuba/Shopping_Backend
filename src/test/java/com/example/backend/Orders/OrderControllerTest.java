package com.example.backend.Orders;

import com.example.backend.SecurityConfig.CustomUserDetailsService;
import com.example.backend.User.UserService;
import com.example.backend.Utils.JwtHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Controller-layer tests using standalone MockMvc (no Spring context overhead).
 *
 * Security rules (hasRole ADMIN) are covered at the HTTP layer by
 * SecurityConfig.java and verified via the request matchers; those rules
 * cannot be tested without a full security filter chain.  The tests here
 * focus on controller logic: correct delegation to OrderService and
 * the right HTTP status/payload for each outcome.
 *
 * Role-based access control (USER gets 403, unauthenticated gets 403) is
 * documented in SecurityConfig and enforced by requestMatchers — see
 * GET  /api/v1/orders              => hasRole("ADMIN")
 * PUT  /api/v1/orders/{id}/status  => hasRole("ADMIN")
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
            .build();
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

        when(orderService.updateOrderStatus(orderId, OrderStatus.SHIPPED)).thenReturn(null);

        mockMvc.perform(put("/api/v1/orders/" + orderId + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isNotFound());
    }
}

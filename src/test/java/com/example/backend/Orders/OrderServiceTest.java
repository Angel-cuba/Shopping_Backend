package com.example.backend.Orders;

import com.example.backend.OrderDetails.OrderDetails;
import com.example.backend.OrderDetails.OrderDetailsRepository;
import com.example.backend.Products.ProductRepository;
import com.example.backend.Products.Products;
import com.example.backend.User.Role;
import com.example.backend.User.User;
import com.example.backend.User.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private OrderRepository        repository;
    @Mock private UserRepository         userRepository;
    @Mock private ProductRepository      productRepository;
    @Mock private OrderDetailsRepository orderDetailsRepository;

    @InjectMocks
    private OrderService orderService;

    // ── Builders ───────────────────────────────────────────────────────────────

    private User buildUser(UUID id, String firstname, String lastname, String email) {
        User user = new User();
        user.setId(id);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setUsername("user_" + firstname.toLowerCase());
        user.setPhone("000-000-0000");
        user.setPassword("hashed");
        user.setRole(Role.USER);
        return user;
    }

    private Order buildOrder(UUID id, User user, OrderStatus status) {
        Order order = new Order();
        order.setId(id);
        order.setUser(user);
        order.setOrderDetails(List.of("item-1", "item-2"));
        order.setPaymentType("CARD");
        order.setShippingAddress("123 Main St");
        order.setShippingMethod("DOOR");
        order.setShippingFee(299);
        order.setTotal(10799);
        order.setStatus(status);
        order.setCreatedAt(LocalDateTime.now());
        return order;
    }

    private Products buildProduct(UUID id, String name, int inStock, int price) {
        Products p = new Products();
        p.setId(id);
        p.setName(name);
        p.setInStock(inStock);
        p.setPrice(price);
        p.setDescription("desc");
        p.setImage("img.png");
        p.setCategories("Summer");
        return p;
    }

    // ── findAllOrders ──────────────────────────────────────────────────────────

    @Test
    void findAllOrders_returnsMappedDTOs() {
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        User user1 = buildUser(userId1, "Alice", "Smith", "alice@example.com");
        User user2 = buildUser(userId2, "Bob",   "Jones", "bob@example.com");

        Order order1 = buildOrder(UUID.randomUUID(), user1, OrderStatus.PENDING);
        Order order2 = buildOrder(UUID.randomUUID(), user2, OrderStatus.CONFIRMED);

        when(repository.findAllWithUserOrderByCreatedAtDesc()).thenReturn(List.of(order1, order2));

        List<AdminOrderDTO> result = orderService.findAllOrders();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).userFirstname()).isEqualTo("Alice");
        assertThat(result.get(0).status()).isEqualTo(OrderStatus.PENDING);
        assertThat(result.get(0).total()).isEqualTo(10799);
        assertThat(result.get(1).userFirstname()).isEqualTo("Bob");
        assertThat(result.get(1).status()).isEqualTo(OrderStatus.CONFIRMED);
    }

    // ── updateOrderStatus ──────────────────────────────────────────────────────

    @Test
    void updateOrderStatus_happyPath() {
        UUID orderId = UUID.randomUUID();
        User user = buildUser(UUID.randomUUID(), "Carol", "White", "carol@example.com");
        Order order = buildOrder(orderId, user, OrderStatus.PENDING);

        when(repository.findById(orderId)).thenReturn(Optional.of(order));
        when(repository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        AdminOrderDTO result = orderService.updateOrderStatus(orderId, OrderStatus.CONFIRMED);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(orderId);
        assertThat(result.status()).isEqualTo(OrderStatus.CONFIRMED);
        assertThat(result.userEmail()).isEqualTo("carol@example.com");
    }

    @Test
    void updateOrderStatus_notFound_returnsNull() {
        UUID orderId = UUID.randomUUID();
        when(repository.findById(orderId)).thenReturn(Optional.empty());

        AdminOrderDTO result = orderService.updateOrderStatus(orderId, OrderStatus.SHIPPED);

        assertThat(result).isNull();
    }

    // ── placeOrder ─────────────────────────────────────────────────────────────

    @Test
    void placeOrder_happyPath_decrementsStockCreatesDetailsAndOrder() {
        UUID userId    = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        User user      = buildUser(userId, "Dan", "Brown", "dan@example.com");
        Products product = buildProduct(productId, "Air Max 90", 10, 9900);

        PlaceOrderRequest.OrderItemRequest item =
                new PlaceOrderRequest.OrderItemRequest(productId, "Crimson", "img.png", "9", 9900, 2);
        PlaceOrderRequest req = new PlaceOrderRequest(
                userId, List.of(item), "Visa", "1 Main St", "DOOR", 299, 20099);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Products.class))).thenAnswer(inv -> inv.getArgument(0));

        OrderDetails savedDetail = new OrderDetails();
        savedDetail.setId(UUID.randomUUID());
        when(orderDetailsRepository.save(any(OrderDetails.class))).thenReturn(savedDetail);

        Order savedOrder = buildOrder(UUID.randomUUID(), user, OrderStatus.PENDING);
        savedOrder.setPaymentType("Visa");
        savedOrder.setTotal(20099);
        when(repository.save(any(Order.class))).thenReturn(savedOrder);

        AdminOrderDTO result = orderService.placeOrder(req);

        assertThat(result).isNotNull();
        assertThat(result.paymentType()).isEqualTo("Visa");
        assertThat(result.total()).isEqualTo(20099);
        assertThat(result.status()).isEqualTo(OrderStatus.PENDING);
        assertThat(result.userEmail()).isEqualTo("dan@example.com");
        // Stock was decremented in-memory: 10 - 2 = 8
        assertThat(product.getInStock()).isEqualTo(8);
    }

    @Test
    void placeOrder_userNotFound_throwsIllegalArgument() {
        UUID userId    = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        PlaceOrderRequest.OrderItemRequest item =
                new PlaceOrderRequest.OrderItemRequest(productId, "Teal", "img.png", "8", 8000, 1);
        PlaceOrderRequest req = new PlaceOrderRequest(
                userId, List.of(item), "Mastercard", "2 Oak Ave", "PICKUP", 0, 8000);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.placeOrder(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void placeOrder_insufficientStock_throwsIllegalState() {
        UUID userId    = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        User user      = buildUser(userId, "Eve", "Green", "eve@example.com");
        Products product = buildProduct(productId, "Zoom Fly", 1, 12000); // only 1 in stock

        PlaceOrderRequest.OrderItemRequest item =
                new PlaceOrderRequest.OrderItemRequest(productId, "Pink", "img.png", "7", 12000, 3); // requesting 3
        PlaceOrderRequest req = new PlaceOrderRequest(
                userId, List.of(item), "Visa", "3 Elm St", "DOOR", 299, 36299);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> orderService.placeOrder(req))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Insufficient stock");
    }

    @Test
    void placeOrder_productNotFound_throwsIllegalArgument() {
        UUID userId    = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        User user      = buildUser(userId, "Frank", "Lee", "frank@example.com");

        PlaceOrderRequest.OrderItemRequest item =
                new PlaceOrderRequest.OrderItemRequest(productId, "Maroon", "img.png", "10", 9000, 1);
        PlaceOrderRequest req = new PlaceOrderRequest(
                userId, List.of(item), "Amex", "4 Pine Rd", "PICKUP", 0, 9000);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.placeOrder(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product not found");
    }
}

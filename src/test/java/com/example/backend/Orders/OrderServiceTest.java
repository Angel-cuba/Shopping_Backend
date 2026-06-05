package com.example.backend.Orders;

import com.example.backend.User.Role;
import com.example.backend.User.User;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository repository;

    @InjectMocks
    private OrderService orderService;

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
        order.setShippingMethod("STANDARD");
        order.setShippingFee(500);
        order.setTotal(10500);
        order.setStatus(status);
        order.setCreatedAt(LocalDateTime.now());
        return order;
    }

    @Test
    void findAllOrders_returnsMappedDTOs() {
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        User user1 = buildUser(userId1, "Alice", "Smith", "alice@example.com");
        User user2 = buildUser(userId2, "Bob", "Jones", "bob@example.com");

        Order order1 = buildOrder(UUID.randomUUID(), user1, OrderStatus.PENDING);
        Order order2 = buildOrder(UUID.randomUUID(), user2, OrderStatus.CONFIRMED);

        when(repository.findAllWithUserOrderByCreatedAtDesc()).thenReturn(List.of(order1, order2));

        List<AdminOrderDTO> result = orderService.findAllOrders();

        assertThat(result).hasSize(2);

        AdminOrderDTO dto1 = result.get(0);
        assertThat(dto1.userFirstname()).isEqualTo("Alice");
        assertThat(dto1.userLastname()).isEqualTo("Smith");
        assertThat(dto1.userEmail()).isEqualTo("alice@example.com");
        assertThat(dto1.userId()).isEqualTo(userId1);
        assertThat(dto1.status()).isEqualTo(OrderStatus.PENDING);
        assertThat(dto1.paymentType()).isEqualTo("CARD");
        assertThat(dto1.total()).isEqualTo(10500);

        AdminOrderDTO dto2 = result.get(1);
        assertThat(dto2.userFirstname()).isEqualTo("Bob");
        assertThat(dto2.status()).isEqualTo(OrderStatus.CONFIRMED);
    }

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
}

package com.example.backend.Orders;

import com.example.backend.OrderDetails.OrderDetails;
import com.example.backend.OrderDetails.OrderDetailsRepository;
import com.example.backend.Products.ProductRepository;
import com.example.backend.Products.Products;
import com.example.backend.User.User;
import com.example.backend.User.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository        repository;
    private final UserRepository         userRepository;
    private final ProductRepository      productRepository;
    private final OrderDetailsRepository orderDetailsRepository;

    public OrderService(OrderRepository repository,
                        UserRepository userRepository,
                        ProductRepository productRepository,
                        OrderDetailsRepository orderDetailsRepository) {
        this.repository              = repository;
        this.userRepository          = userRepository;
        this.productRepository       = productRepository;
        this.orderDetailsRepository  = orderDetailsRepository;
    }

    public Order saveOrder(Order order) {
        return repository.save(order);
    }

    public List<Order> getOrdersByUserId(UUID userId) {
        return repository.findOrdersByUserId(userId);
    }

    public List<AdminOrderDTO> findAllOrders() {
        return repository.findAllWithUserOrderByCreatedAtDesc()
                .stream()
                .map(AdminOrderDTO::from)
                .toList();
    }

    public AdminOrderDTO updateOrderStatus(UUID id, OrderStatus status) {
        Order order = repository.findById(id).orElse(null);
        if (order == null) return null;
        order.setStatus(status);
        return AdminOrderDTO.from(repository.save(order));
    }

    /**
     * Place a complete order in a single DB transaction:
     * 1. Validate stock for every item.
     * 2. Decrement stock for every item.
     * 3. Create OrderDetails rows.
     * 4. Create the Order row.
     *
     * Any failure rolls back all changes automatically.
     */
    @Transactional
    public AdminOrderDTO placeOrder(PlaceOrderRequest req) {
        // 1. Resolve user
        User user = userRepository.findById(req.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + req.userId()));

        List<String> detailIds = new ArrayList<>();

        for (PlaceOrderRequest.OrderItemRequest item : req.items()) {
            // 2. Validate & decrement stock (throws if insufficient — triggers rollback)
            Products product = productRepository.findById(item.productId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.productId()));

            if (product.getInStock() < item.quantity()) {
                throw new IllegalStateException(
                        "Insufficient stock for '" + product.getName() + "': "
                        + "requested " + item.quantity() + ", available " + product.getInStock());
            }
            product.setInStock(product.getInStock() - item.quantity());
            productRepository.save(product);

            // 3. Create OrderDetails row
            OrderDetails detail = new OrderDetails();
            detail.setProductId(item.productId().toString());
            detail.setVariant(item.variant());
            detail.setImage(item.image());
            detail.setSize(item.size());
            detail.setPrice(item.price());
            detail.setQuantity(item.quantity());
            detail.setUser(user);
            OrderDetails saved = orderDetailsRepository.save(detail);
            detailIds.add(saved.getId().toString());
        }

        // 4. Create Order row
        Order order = new Order();
        order.setUser(user);
        order.setOrderDetails(detailIds);
        order.setPaymentType(req.paymentType());
        order.setShippingAddress(req.shippingAddress());
        order.setShippingMethod(req.shippingMethod());
        order.setShippingFee(req.shippingFee());
        order.setTotal(req.total());
        order.setStatus(OrderStatus.PENDING);

        return AdminOrderDTO.from(repository.save(order));
    }
}

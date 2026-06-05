package com.example.backend.Orders;

import com.example.backend.Exceptions.InsufficientStockException;
import com.example.backend.Exceptions.NotFoundException;
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
        Order order = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found: " + id));
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
    /**
     * Place a complete order in a single DB transaction:
     * Phase 1 — resolve all products and validate stock for every item before mutating anything,
     *            so a failure on item N does not leave items 1..N-1 in a decremented state.
     * Phase 2 — decrement stock, persist OrderDetails rows.
     * Phase 3 — create the Order row.
     *
     * Any unchecked exception triggers a full rollback via @Transactional.
     */
    @Transactional
    public AdminOrderDTO placeOrder(PlaceOrderRequest req) {
        // Phase 1 — resolve user and validate all stock before touching the DB
        User user = userRepository.findById(req.userId())
                .orElseThrow(() -> new NotFoundException("User not found: " + req.userId()));

        List<Products> resolvedProducts = new ArrayList<>();
        for (PlaceOrderRequest.OrderItemRequest item : req.items()) {
            Products product = productRepository.findById(item.productId())
                    .orElseThrow(() -> new NotFoundException("Product not found: " + item.productId()));
            if (product.getInStock() < item.quantity()) {
                throw new InsufficientStockException(
                        "Insufficient stock for '" + product.getName() + "': "
                        + "requested " + item.quantity() + ", available " + product.getInStock());
            }
            resolvedProducts.add(product);
        }

        // Phase 2 — decrement stock and create OrderDetails rows
        List<String> detailIds = new ArrayList<>();
        for (int i = 0; i < req.items().size(); i++) {
            PlaceOrderRequest.OrderItemRequest item = req.items().get(i);
            Products product = resolvedProducts.get(i);

            product.setInStock(product.getInStock() - item.quantity());
            productRepository.save(product);

            OrderDetails detail = new OrderDetails();
            detail.setProductId(item.productId().toString());
            detail.setVariant(item.variant());
            detail.setImage(item.image());
            detail.setSize(item.size());
            detail.setPrice(item.price());
            detail.setQuantity(item.quantity());
            detail.setUser(user);
            detailIds.add(orderDetailsRepository.save(detail).getId().toString());
        }

        // Phase 3 — create the Order row
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

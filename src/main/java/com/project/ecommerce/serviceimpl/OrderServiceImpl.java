package com.project.ecommerce.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.ecommerce.entity.Cart;
import com.project.ecommerce.entity.CartItem;
import com.project.ecommerce.entity.Order;
import com.project.ecommerce.entity.OrderItem;
import com.project.ecommerce.entity.User;
import com.project.ecommerce.repository.CartRepository;
import com.project.ecommerce.repository.OrderRepository;
import com.project.ecommerce.repository.ProductRepository;
import com.project.ecommerce.repository.UserRepository;

@Service
public class OrderServiceImpl {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;

    public Order placeOrder(Long userId, String shippingAddress, List<Long> cartItemIds) {
        // Find the cart for the user
    	User user = userRepository.findById(userId).get();
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user with ID: " + userId));

        // Check if the cart has the requested items
        if (cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty. Please add products to the cart before placing an order.");
        }

        // Create new order
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setShippingAddress(shippingAddress);
        order.setStatus("Placed");

        // Add items from the cart to the order
        for (Long cartItemId : cartItemIds) {
            CartItem cartItem = cart.getCartItems().stream()
                    .filter(item -> item.getId().equals(cartItemId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Cart item with ID " + cartItemId + " not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());

            order.getOrderItems().add(orderItem);
        }

        // Save the order
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByCustomer(Long customerId) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getUser().getId().equals(customerId))
                .collect(Collectors.toList());
    }
}

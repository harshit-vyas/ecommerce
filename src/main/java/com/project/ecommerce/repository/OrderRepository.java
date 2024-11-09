package com.project.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.ecommerce.entity.Order;
import com.project.ecommerce.entity.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Find all orders by User
    List<Order> findByUser(User user);

    // Find Order by Order ID
//    Optional<Order> findByOrderId(Long orderId);

    // Find Orders by Status (e.g., "Pending", "Shipped")
    List<Order> findByStatus(String status);
}

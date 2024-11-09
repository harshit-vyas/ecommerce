package com.project.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.ecommerce.entity.Cart;
import com.project.ecommerce.entity.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // Find Cart by User
    Optional<Cart> findByUser(User user);

    // Find Cart by User and Cart ID
    Optional<Cart> findByUserAndId(User user, Long cartId);

}

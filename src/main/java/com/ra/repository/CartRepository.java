package com.ra.repository;

import com.ra.model.entity.Cart;
import com.ra.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart,Long> {
Cart findByUser(User user);
}

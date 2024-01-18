package com.ra.repository;

import com.ra.model.dto.response.ProductResponseDTO;
import com.ra.model.entity.Cart;
import com.ra.model.entity.CartItem;
import com.ra.model.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Transactional
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByCart_Id(Long id);

    @Modifying
    @Query("delete FROM CartItem c WHERE c.cart.id = :id")
    void deleteCartItemByCartId(@Param("id") Long id);

    CartItem findByCartAndProduct(Cart cart, Product product);

    Boolean existsCartItemByCartAndProduct(Cart cart, Product product);

    CartItem findCartItemById(Long id);

//List<CartItem> findCartItemByCart_Id(Long id);

    List<CartItem> findAllByCart(Cart cart);


}

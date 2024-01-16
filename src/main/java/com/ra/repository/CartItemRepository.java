package com.ra.repository;

import com.ra.model.entity.Cart;
import com.ra.model.entity.CartItem;
import com.ra.model.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
void deleteAllByCart_Id(Long id);
CartItem findByCartAndProduct(Cart cart,Product product);

Boolean existsCartItemByCartAndProduct(Cart cart,Product product);
CartItem findCartItemById(Long id);

//List<CartItem> findCartItemByCart_Id(Long id);

    List<CartItem> findAllByCart(Cart cart);

}

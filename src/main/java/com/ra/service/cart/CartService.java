package com.ra.service.cart;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.CartItemRequestDTO;
import com.ra.model.dto.request.CartRequestDTO;
import com.ra.model.dto.response.CartResponseDTO;
import com.ra.model.entity.Cart;
import com.ra.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CartService {
    Page<CartResponseDTO> getAll(Pageable pageable);

    List<CartResponseDTO> findAll();

    //    CartResponseDTO findCartByUserId(Long id);
    CartResponseDTO findById(Long id);

    void addToCart(Long id, CartItemRequestDTO cartItemRequestDTO) throws CustomException;



//    void clearAllCartItemsByUser(Cart cart);
    void clearAllCartItemsByCartId(Long id);



}

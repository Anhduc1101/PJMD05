package com.ra.service.cartItem;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.CartItemRequestDTO;
import com.ra.model.dto.response.CartItemResponseDTO;
import com.ra.model.entity.Cart;
import com.ra.model.entity.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CartItemService {
    Page<CartItemResponseDTO> getAll(Pageable pageable);

    List<CartItemResponseDTO> findAll();

    void deleteCartItem(Long id);

    void deleteAllCartItemByCartId(Long id) throws CustomException;

    CartItemResponseDTO save(CartItemRequestDTO cartItemRequestDTO);

    void deleteCartItemByCart(Cart cart);

//    List<CartItem> findAllCartItemByCartId(Long id);
}

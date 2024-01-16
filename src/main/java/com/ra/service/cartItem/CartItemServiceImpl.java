package com.ra.service.cartItem;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.CartItemRequestDTO;
import com.ra.model.dto.response.CartItemResponseDTO;
import com.ra.model.dto.response.CartResponseDTO;
import com.ra.model.entity.Cart;
import com.ra.model.entity.CartItem;
import com.ra.repository.CartItemRepository;
import com.ra.service.cart.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CartItemServiceImpl implements CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CartService cartService;


    @Override
    public Page<CartItemResponseDTO> getAll(Pageable pageable) {
        Page<CartItem> cartItemPage = cartItemRepository.findAll(pageable);
        return cartItemPage.map(CartItemResponseDTO::new);
    }

    @Override
    public List<CartItemResponseDTO> findAll() {
        List<CartItem> cartItemList = cartItemRepository.findAll();
        return cartItemList.stream().map(CartItemResponseDTO::new).toList();
    }

    @Override
    public void deleteCartItem(Long id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public void deleteAllCartItemByCartId(Long id) throws CustomException {
        CartResponseDTO cartResponseDTO = cartService.findById(id);
        if (cartResponseDTO != null) {
            if (!cartResponseDTO.getCartItem().isEmpty()) {
                cartItemRepository.deleteAllByCart_Id(id);
            } else {
                throw new CustomException("Cart is blank");
            }
            throw new CustomException("Not found");
        }
    }

    @Override
    public CartItemResponseDTO save(CartItemRequestDTO cartItemRequestDTO) {
        if (cartItemRequestDTO.getProductId() == null) {
            CartItem cartItem = new CartItem();
            cartItem.setId(cartItemRequestDTO.getProductId());
            cartItem.setQuantity(cartItemRequestDTO.getQuantity());
            cartItemRepository.save(cartItem);
            return new CartItemResponseDTO(cartItem);
        }
        return null;
    }

//    @Override
//    public List<CartItem> findAllCartItemByCartId(Long id) {
//        return cartItemRepository.findCartItemByCart_Id(id);
//    }
}

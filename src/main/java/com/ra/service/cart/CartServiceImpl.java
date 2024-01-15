package com.ra.service.cart;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.CartItemRequestDTO;
import com.ra.model.dto.request.CartRequestDTO;
import com.ra.model.dto.response.CartItemResponseDTO;
import com.ra.model.dto.response.CartResponseDTO;
import com.ra.model.dto.response.UserResponseDTO;
import com.ra.model.entity.Cart;
import com.ra.model.entity.CartItem;
import com.ra.model.entity.Product;
import com.ra.model.entity.User;
import com.ra.repository.CartItemRepository;
import com.ra.repository.CartRepository;
import com.ra.repository.ProductRepository;
import com.ra.repository.UserRepository;
import com.ra.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Page<CartResponseDTO> getAll(Pageable pageable) {
        Page<Cart> cartPage = cartRepository.findAll(pageable);
        return cartPage.map(CartResponseDTO::new);
    }

    @Override
    public List<CartResponseDTO> findAll() {
        List<Cart> cartList = cartRepository.findAll();
        return cartList.stream().map(CartResponseDTO::new).toList();
    }


    @Override
    public CartResponseDTO findById(Long id) {
        Optional<Cart> cartOptional = cartRepository.findById(id);
        return cartOptional.map(CartResponseDTO::new).orElse(null);
    }


    @Override
    public void addToCart(Long id, CartItemRequestDTO cartItemRequestDTO) throws CustomException {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException("User is not found"));
        Cart cart = cartRepository.findByUser(user);
        Product product = productRepository.findById(cartItemRequestDTO.getProductId()).orElseThrow(() -> new CustomException("Product is not found"));

        if (cart == null) {
            Cart newCart = new Cart();
            newCart.setUser(user);
            cartRepository.save(newCart);
        }
        CartItem cartItem;
        if (cartItemRepository.existsCartItemByCartAndProduct(cart, product)) {
            cartItem = cartItemRepository.findByCartAndProduct(cart, product);
            cartItem.setQuantity(cartItem.getQuantity() + cartItemRequestDTO.getQuantity());
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setQuantity(cartItemRequestDTO.getQuantity());
            cartItem.setPrice(product.getPrice());
            cartItem.setProduct(product);
        }
        cartItemRepository.save(cartItem);
    }

    @Override
    public void clearAllCartItems() {
        cartItemRepository.deleteAll();
    }

}
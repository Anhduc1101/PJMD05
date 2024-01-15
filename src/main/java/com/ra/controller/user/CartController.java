package com.ra.controller.user;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.CartItemRequestDTO;
import com.ra.model.dto.request.CartRequestDTO;
import com.ra.model.dto.response.CartItemResponseDTO;
import com.ra.model.dto.response.CartResponseDTO;
import com.ra.model.dto.response.UserResponseDTO;
import com.ra.model.entity.CartItem;
import com.ra.model.entity.User;
import com.ra.repository.CartItemRepository;
import com.ra.security.user_principle.UserDetailService;
import com.ra.service.cart.CartService;
import com.ra.service.cartItem.CartItemService;
import com.ra.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
public class CartController {
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/shopping-cart")
    public ResponseEntity<?> getListCartItems() {
        List<CartItemResponseDTO> cartItemResponseDTOS = cartItemService.findAll();
        return new ResponseEntity<>(cartItemResponseDTOS, HttpStatus.OK);
    }

    @PostMapping("/shopping-cart")
    public ResponseEntity<?> addToCart(@RequestBody CartItemRequestDTO cartItemRequestDTO, Authentication authentication) throws CustomException {
        Long userId = userDetailService.getUserIdFromAuthentication(authentication);
        cartService.addToCart(userId, cartItemRequestDTO);
        return new ResponseEntity<>("Add to cart successfully !!!", HttpStatus.OK);
    }

    @DeleteMapping("/shopping-cart")
    public ResponseEntity<?> clearAll(){
        cartService.clearAllCartItems();
        return new ResponseEntity<>("All cart items had been deleted !!!",HttpStatus.OK);
    }

    @DeleteMapping("/shopping-cart/{id}")
    public ResponseEntity<?> deleteCartItem(@PathVariable("id") Long id){
        CartItem cartItem=cartItemRepository.findCartItemById(id);
        if (cartItem!=null){
            cartItemRepository.deleteById(id);
        return new ResponseEntity<>("Deleted successfully !!!",HttpStatus.OK);
        }
       return new ResponseEntity<>("Not Found",HttpStatus.NOT_FOUND);
    }

    @GetMapping("/shopping-cart/{id}")
    public ResponseEntity<?> getCartByUserId(@PathVariable("id") Long id){
        UserResponseDTO userResponseDTO=userService.findById(id); //check lai id, id = 1 van ra
        if (userResponseDTO!=null){
            List<CartItemResponseDTO> cartItemResponseDTOS = cartItemService.findAll();
            return new ResponseEntity<>(cartItemResponseDTOS, HttpStatus.OK);
        }
        return new ResponseEntity<>("Not found",HttpStatus.NOT_FOUND);
    }

    @PostMapping("/shopping-cart/check-out")
    public ResponseEntity<?> checkOut(@RequestBody CartRequestDTO cartRequestDTO){

        return null;
    }
}

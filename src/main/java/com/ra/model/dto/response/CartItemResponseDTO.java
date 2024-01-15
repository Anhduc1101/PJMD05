package com.ra.model.dto.response;

import com.ra.model.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CartItemResponseDTO {
    private Long id;
    private Long cartId;
    private int quantity;
    private float price;
    private Long productId;

    public CartItemResponseDTO(CartItem cartItem) {
        this.id = cartItem.getId();
        this.cartId = cartItem.getCart().getId();
        this.quantity = cartItem.getQuantity();
        this.price = cartItem.getPrice();
        this.productId = cartItem.getProduct().getId();
    }
}

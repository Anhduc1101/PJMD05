package com.ra.model.dto.response;
import com.ra.model.entity.Cart;
import com.ra.model.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CartResponseDTO {
    private Long id;
    private Long userId;
    private Set<CartItem> cartItem;

    public CartResponseDTO(Cart cart) {
        this.id = cart.getId();
        this.userId = cart.getUser().getId();
        this.cartItem = cart.getCartItem();
    }
}

package com.ra.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CartItemRequestDTO {
    private int quantity=1;
    @NotEmpty(message = "can not leave blank")
    private Long productId;
}

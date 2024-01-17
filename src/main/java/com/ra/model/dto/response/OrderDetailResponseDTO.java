package com.ra.model.dto.response;

import com.ra.model.entity.OrderDetail;
import com.ra.model.entity.Orders;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetailResponseDTO {
    private Long id;
    private float price;
    private int quantity;
    private Long productId;
    private Long ordersId;

    public OrderDetailResponseDTO(OrderDetail orderDetail) {
        this.id = orderDetail.getId();
        this.price = orderDetail.getPrice();
        this.quantity = orderDetail.getQuantity();
        this.productId = orderDetail.getProduct().getId();
        this.ordersId = orderDetail.getOrders().getId();
    }
}

package com.ra.model.dto.response;

import com.ra.model.entity.OrderDetail;
import com.ra.model.entity.Orders;
import com.ra.model.entity.Product;
import com.ra.model.entity.Role;
import jakarta.persistence.Column;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HistoryResponseDTO {
    private Long id;
    private float price;
    private int quantity;
    private int productId;
    private String img;
    private float total;
    private Date createAt;

    public HistoryResponseDTO(Orders orders) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.productId = productId;
        this.img = img;
        this.total = total;
        this.createAt = createAt;
    }
}

package com.ra.model.dto.request;

import com.ra.model.entity.Orders;
import com.ra.model.entity.Product;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetailRequestDTO {
    private Long id;
    private float price;
    private int quantity;
    private Product product;
    private Orders orders;

}

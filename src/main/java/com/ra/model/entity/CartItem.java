package com.ra.model.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "cartId",referencedColumnName = "id")
    private Cart cart;
    private int quantity;
    private float price;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "productId",referencedColumnName = "id")
    private Product product;

}

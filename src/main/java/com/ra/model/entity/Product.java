package com.ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String productName;
    @Column(columnDefinition = "Boolean default true")
    private Boolean status = true;
    private String img;
    private float price;

    @ManyToOne
    @JoinColumn(name = "catId", referencedColumnName = "id")
    private Category category;

    @OneToMany(mappedBy = "product",cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "product",fetch = FetchType.EAGER)
    private Set<CartItem> cartItemList;

    @ManyToOne
    @JoinColumn(name = "wishListId",referencedColumnName = "id")
    private WishList wishList;
}

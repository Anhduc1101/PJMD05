package com.ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class WishList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId",referencedColumnName = "id")
    @JsonIgnore
    private User user;
    @OneToMany(mappedBy = "wishList",cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Product> products;

}

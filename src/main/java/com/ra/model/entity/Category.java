package com.ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String categoryName;
    @Column(columnDefinition = "Boolean default true")
    private Boolean status=true;

    @OneToMany(mappedBy = "category",cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Product> products;

    public Category(Long id, String categoryName, Boolean status) {
        this.id = id;
        this.categoryName = categoryName;
        this.status = status;
    }
}

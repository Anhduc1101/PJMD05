package com.ra.model.dto.response;

import com.ra.model.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class ProductResponseDTO {
    private Long id;
    private String productName;
    private Boolean status = true;
    private String img;
    private float price;
    private String categoryName;

    public ProductResponseDTO(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.status = product.getStatus();
        this.img = product.getImg();
        this.price = product.getPrice();
        this.categoryName=product.getCategory().getCategoryName();
    }
}

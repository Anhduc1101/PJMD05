package com.ra.model.dto.response;

import com.ra.model.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CategoryResponseDTO {
    private Long id;
    private String categoryName;
    private Boolean status = true;

    public CategoryResponseDTO(Category category) {
        this.id = category.getId();
        this.categoryName = category.getCategoryName();
        this.status = category.getStatus();
    }

}

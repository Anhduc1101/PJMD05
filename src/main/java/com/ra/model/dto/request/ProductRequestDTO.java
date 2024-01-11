package com.ra.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ra.model.entity.Category;
import com.ra.model.entity.OrderDetail;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductRequestDTO {
    private Long id;
    private String productName;
    private Boolean status = true;
    private MultipartFile file;
    private float price;
    private Long categoryId;
}

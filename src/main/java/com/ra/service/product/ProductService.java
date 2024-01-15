package com.ra.service.product;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.ProductRequestDTO;
import com.ra.model.dto.response.ProductResponseDTO;
import com.ra.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    List<ProductResponseDTO> getAll();
    ProductResponseDTO saveOrUpdate(ProductRequestDTO productRequestDTO) throws CustomException;
    void delete(Long id);
    ProductResponseDTO findById(Long id);

    Page<ProductResponseDTO> getAll(Pageable pageable);

    Page<ProductResponseDTO> searchByName(Pageable pageable, String name);

    void changeStatus(Long id);

    List<ProductResponseDTO> getProductListByCategoryId(Long id);

}

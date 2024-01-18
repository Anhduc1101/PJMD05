package com.ra.service.category;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.CategoryRequestDTO;
import com.ra.model.dto.response.CategoryResponseDTO;
import com.ra.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    List<CategoryResponseDTO> findAll();
    Page<Category> getAll(Pageable pageable);
    Page<Category> searchByName(Pageable pageable,String name);
    void delete(Long id);
    CategoryResponseDTO saveOrUpdate(CategoryRequestDTO categoryRequestDTO) throws CustomException;
    CategoryResponseDTO findById(Long id);
    void changeStatus(Long id);

    Boolean checkProductByCategoryId(Long id);

}

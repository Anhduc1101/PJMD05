package com.ra.service.category;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.CategoryRequestDTO;
import com.ra.model.dto.response.CategoryResponseDTO;
import com.ra.model.entity.Category;
import com.ra.repository.CategoryRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponseDTO> findAll() {
        List<Category> categoryList=categoryRepository.findAll();
        return categoryList.stream().map(CategoryResponseDTO::new).toList();
    }

    @Override
    public Page<Category> getAll(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        return categoryPage.map(category -> new Category(category.getId(), category.getCategoryName(), category.getStatus()));
    }

    @Override
    public Page<Category> searchByName(Pageable pageable, String name) {
        Page<Category> categoryPage = categoryRepository.findAllByCategoryNameContainingIgnoreCase(pageable, name);
        return categoryPage.map(category -> new Category(category.getId(), category.getCategoryName(), category.getStatus()));

    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryResponseDTO saveOrUpdate(CategoryRequestDTO categoryRequestDTO) throws CustomException {
        Category newCat;
        if (categoryRequestDTO.getId() == null) {
            if (categoryRepository.existsByCategoryName(categoryRequestDTO.getCategoryName())) {
                throw new CustomException("Category name had been exist!");
            }
            if (StringUtils.isBlank(categoryRequestDTO.getCategoryName())) {
                throw new CustomException("Category name must not be blank");
            }
            newCat = new Category();
        } else {
            newCat = categoryRepository.findById(categoryRequestDTO.getId()).orElse(null);
        }
        newCat.setCategoryName(categoryRequestDTO.getCategoryName());
        newCat.setStatus(categoryRequestDTO.getStatus());
        categoryRepository.save(newCat);
        return new CategoryResponseDTO(newCat);
    }


    @Override
    public CategoryResponseDTO findById(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        return categoryOptional.map(category -> new CategoryResponseDTO(category.getId(), category.getCategoryName(), category.getStatus())).orElse(null);
    }

    //    cach 2: change status
    @Override
    public void changeStatus(Long id) {
        CategoryResponseDTO categoryResponseDTO = findById(id);
        if (categoryResponseDTO != null) {
            categoryRepository.changeStatus(id);
        }
    }

}

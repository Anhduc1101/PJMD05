package com.ra.controller.admin;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.CategoryRequestDTO;
import com.ra.model.dto.response.CategoryResponseDTO;
import com.ra.model.entity.Category;
import com.ra.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<?> getListCategories() {
        List<CategoryResponseDTO> categoryResponseDTOList=categoryService.findAll();
        return new ResponseEntity<>(categoryResponseDTOList, HttpStatus.OK);
    }

    @GetMapping("/categories/sort+pagination")
    public ResponseEntity<Page<Category>> getListCategories(@RequestParam(name = "page", defaultValue = "0") int page,
                                                            @RequestParam(name = "size", defaultValue = "5") int size,
                                                            @RequestParam(name = "sort", defaultValue = "id") String sort,
                                                            @RequestParam(name = "order", defaultValue = "asc") String order) {
        Pageable pageable;
        if (order.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        Page<Category> categoryPage = categoryService.getAll(pageable);
        return new ResponseEntity<>(categoryPage, HttpStatus.OK);
    }

    @GetMapping("/categories/search")
    public ResponseEntity<Page<Category>> search(@RequestParam(name = "page", defaultValue = "0") int page,
                                                 @RequestParam(name = "size", defaultValue = "5") int size,
                                                 @RequestParam(name = "sort", defaultValue = "id") String sort,
                                                 @RequestParam(name = "order", defaultValue = "asc") String order,
                                                 @RequestParam(name = "search") String search) {
        Pageable pageable;
        if (order.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        Page<Category> categoryPage = categoryService.searchByName(pageable, search);
        return new ResponseEntity<>(categoryPage, HttpStatus.OK);
    }

    @PostMapping("/categories")
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequestDTO categoryRequestDTO) throws CustomException {
        CategoryResponseDTO categoryResponseDTO = categoryService.saveOrUpdate(categoryRequestDTO);
        return new ResponseEntity<>(categoryResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<?> findCategoryById(@PathVariable("id") Long id) {
        CategoryResponseDTO category = categoryService.findById(id);
        if (category != null) {
            return new ResponseEntity<>(category, HttpStatus.OK);
        }
        return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Long id,
                                            @RequestBody CategoryRequestDTO categoryRequestDTO) throws CustomException {
        CategoryResponseDTO newCat = categoryService.findById(id);
        categoryRequestDTO.setId(newCat.getId());
        CategoryResponseDTO updateCat = categoryService.saveOrUpdate(categoryRequestDTO);
        return new ResponseEntity<>(updateCat, HttpStatus.OK);
    }

    //    cach 1
    @PatchMapping("/categories/{id}")
    public ResponseEntity<?> changeCategoryStatus(@PathVariable("id") Long id) throws CustomException {
        CategoryResponseDTO cat = categoryService.findById(id);
        if (cat != null) {
            CategoryRequestDTO categoryRequestDTO = CategoryRequestDTO.builder().id(cat.getId()).categoryName(cat.getCategoryName()).status(!cat.getStatus()).build();

            CategoryResponseDTO categoryResponseDTO = categoryService.saveOrUpdate(categoryRequestDTO);
            return new ResponseEntity<>(categoryResponseDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
    }

    //    cach 2: use query
//    @PatchMapping("/categories/{id}")
//    public ResponseEntity<?> changeCategoryStatus(@PathVariable("id") Long id) throws CustomException {
//        categoryService.changeStatus(id);
//        CategoryResponseDTO categoryResponseDTO = categoryService.findById(id);
//        return new ResponseEntity<>(categoryResponseDTO, HttpStatus.OK);
//    }


    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id) {
        CategoryResponseDTO cat = categoryService.findById(id);
        if (cat != null) {
            Boolean checkProductByCategoryId=categoryService.checkProductByCategoryId(id);
            if (!checkProductByCategoryId){
            categoryService.delete(id);
            return new ResponseEntity<>("Delete successfully !!!", HttpStatus.OK);
            }
            return new ResponseEntity<>("This category currently has products, can not delete it !!!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
    }
}

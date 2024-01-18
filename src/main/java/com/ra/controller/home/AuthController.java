package com.ra.controller.home;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.UserRequestDTO;
import com.ra.model.dto.response.*;
import com.ra.model.entity.Category;
import com.ra.model.entity.User;
import com.ra.service.category.CategoryService;
import com.ra.service.product.ProductService;
import com.ra.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    //    Đăng kí tài khoản người dùng
    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        return new ResponseEntity<>(userService.register(user), HttpStatus.OK);
    }


    //    Đăng nhập tài khoản bằng username và password
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDTO userRequestDTO) throws CustomException {
        UserLoginResponseDTO userLoginResponseDTO = userService.login(userRequestDTO);
        if (userLoginResponseDTO.getStatus()){
        return new ResponseEntity<>(userLoginResponseDTO, HttpStatus.OK);
        }
        throw new CustomException("Your Account had been locked");
    }

    //    Danh sách danh mục được bán
    @GetMapping("/categories")
    public ResponseEntity<?> getListCategories() {
        List<CategoryResponseDTO> categoryResponseDTOList = categoryService.findAll();
        return new ResponseEntity<>(categoryResponseDTOList, HttpStatus.OK);
    }

    @GetMapping("/products")
    public ResponseEntity<?> getListProducts() {
        List<ProductResponseDTO> productResponseDTOList = productService.getAll();
        List<ProductResponseDTO> filteredProducts = productResponseDTOList.stream().filter(ProductResponseDTO::getStatus).toList();
        return new ResponseEntity<>(filteredProducts, HttpStatus.OK);
    }


    //    Danh sách sản phẩm được bán(có phân trang và sắp xếp)
    @GetMapping("/products/sort+pagination")
    public ResponseEntity<Page<ProductResponseDTO>> getListProducts(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                    @RequestParam(name = "size", defaultValue = "5") int size,
                                                                    @RequestParam(name = "sort", defaultValue = "id") String sort,
                                                                    @RequestParam(name = "order", defaultValue = "asc") String order) {
        Pageable pageable;
        if (order.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        Page<ProductResponseDTO> productResponseDTOS = productService.getAll(pageable);
        return new ResponseEntity<>(productResponseDTOS, HttpStatus.OK);
    }

    //    Tìm kiếm sản phẩm theo tên
    @GetMapping("/products/search")
    public ResponseEntity<Page<ProductResponseDTO>> search(@RequestParam(name = "page", defaultValue = "0") int page,
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
        Page<ProductResponseDTO> productResponseDTOS = productService.searchByName(pageable, search);
        return new ResponseEntity<>(productResponseDTOS, HttpStatus.OK);
    }


    //    Danh sách sản phẩm nổi bật
    @GetMapping("/products/featured-products")
    public ResponseEntity<?> getFeaturedProductsList() {
        List<ProductResponseDTO> productResponseDTOList = productService.getAll();
        List<ProductResponseDTO> featuredProducts = productResponseDTOList
                .stream()
                .sorted(Comparator.comparingDouble(ProductResponseDTO::getPrice))
                .limit(2)
                .collect(Collectors.toList());
        if (!featuredProducts.isEmpty()) {
            return new ResponseEntity<>(featuredProducts, HttpStatus.OK);
        }
        return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
    }

    //    Danh sách sản phẩm theo danh mục
    @GetMapping("/products/categories/{id}")
    public ResponseEntity<?> getListProductsByCategoryId(@PathVariable("id") Long id) {
        if (categoryService.findById(id) != null) {
            List<ProductResponseDTO> productResponseDTO = productService.getProductListByCategoryId(id);
            if (!productResponseDTO.isEmpty()) {
                return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            }
        }
        return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
    }

    //    Chi tiết thông tin sản phẩm theo id
    @GetMapping("/products/{id}")
    public ResponseEntity<?> findProductById(@PathVariable("id") Long id) {
        ProductResponseDTO productResponseDTO = productService.findById(id);
        if (productResponseDTO != null) {
            return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
    }
}

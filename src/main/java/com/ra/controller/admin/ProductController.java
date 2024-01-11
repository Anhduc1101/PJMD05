package com.ra.controller.admin;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.ProductRequestDTO;
import com.ra.model.dto.response.ProductResponseDTO;
import com.ra.model.entity.Category;
import com.ra.model.entity.Product;
import com.ra.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/admin")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/products/sort+pagination+search")
    public ResponseEntity<Page<ProductResponseDTO>> getProducts(@RequestParam(name = "page", defaultValue = "0") int page,
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


    @PostMapping("/products")
    public ResponseEntity<?> createProduct(@ModelAttribute("product") ProductRequestDTO productRequestDTO) throws CustomException {
        ProductResponseDTO productResponseDTO = productService.saveOrUpdate(productRequestDTO);
        return new ResponseEntity<>(productResponseDTO, HttpStatus.CREATED);
    }


    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
        ProductResponseDTO pro = productService.findById(id);
        if (pro != null) {
            productService.delete(id);
            return new ResponseEntity<>(pro, HttpStatus.OK);
        }
        return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> findProductById(@PathVariable("id") Long id) {
        ProductResponseDTO productResponseDTO = productService.findById(id);
        if (productResponseDTO != null) {
            return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long id,@RequestBody ProductRequestDTO productRequestDTO) throws CustomException {
        ProductResponseDTO newPro=productService.findById(id);
        productRequestDTO.setId(newPro.getId());
        ProductResponseDTO updatePro=productService.saveOrUpdate(productRequestDTO);
        return new ResponseEntity<>(updatePro,HttpStatus.OK);
    }

//    @PutMapping("/products/{id}")
//    public ResponseEntity<?> updateProduct(@PathVariable("id") Long id, @RequestParam(name = "productName") String name,
//                                           @RequestParam(name = "file") MultipartFile file,
//                                           @RequestBody ProductRequestDTO productRequestDTO) throws CustomException {
//
//        ProductResponseDTO newPro = productService.findById(id);
//        productRequestDTO.setId(newPro.getId());
//        ProductResponseDTO updatePro = productService.saveOrUpdate(productRequestDTO);
//        return new ResponseEntity<>(updatePro, HttpStatus.OK);
//    }

    @PatchMapping("/products/{id}")
    public ResponseEntity<?> changeProductStatus(@PathVariable("id") Long id) {
        productService.changeStatus(id);
        ProductResponseDTO productResponseDTO = productService.findById(id);
        return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
    }
}

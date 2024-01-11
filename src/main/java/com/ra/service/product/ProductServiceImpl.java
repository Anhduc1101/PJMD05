package com.ra.service.product;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.ProductRequestDTO;
import com.ra.model.dto.response.ProductResponseDTO;
import com.ra.model.entity.Category;
import com.ra.model.entity.Product;
import com.ra.repository.CategoryRepository;
import com.ra.repository.ProductRepository;
import com.ra.service.category.CategoryService;
import com.ra.service.upload.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<ProductResponseDTO> getAll() {
        List<Product> productList = productRepository.findAll();
        return productList.stream().map(ProductResponseDTO::new).collect(Collectors.toList());
    }


    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public ProductResponseDTO findById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        return productOptional.map(ProductResponseDTO::new).orElse(null);
    }

    @Override
    public Page<ProductResponseDTO> getAll(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(ProductResponseDTO::new);
    }

    @Override
    public Page<ProductResponseDTO> searchByName(Pageable pageable, String name) {
        Page<Product> productPage=productRepository.findAllByProductNameContainingIgnoreCase(pageable, name);
        return productPage.map(ProductResponseDTO::new);
    }

    @Override
    public void changeStatus(Long id) {
        ProductResponseDTO productResponseDTO = findById(id);
        if (productResponseDTO != null) {
            productRepository.changeStatus(id);
        }
    }

    @Override
    public ProductResponseDTO saveOrUpdate(ProductRequestDTO productRequestDTO) throws CustomException {
        Product newPro;
        if (productRequestDTO.getId()==null){
            if (productRepository.existsByProductName(productRequestDTO.getProductName())){
                throw new CustomException("Product name had been exist");
            }
            newPro=new Product();
        }else {
            newPro=productRepository.findById(productRequestDTO.getId()).orElse(null);
        }
            newPro.setProductName(productRequestDTO.getProductName());
            newPro.setStatus(productRequestDTO.getStatus());
            newPro.setPrice(productRequestDTO.getPrice());
//            String fileName = uploadService.uploadImage(productRequestDTO.getFile());
//            newPro.setImg(fileName);
//            Category category = categoryRepository.findById(productRequestDTO.getCategoryId()).orElse(null);
//            newPro.setCategory(category);
//        newPro.setOrderDetails(productRequestDTO.getOrderDetails());
        MultipartFile file = productRequestDTO.getFile();
        if (file != null && !file.isEmpty()) {
            // Gọi phương thức uploadImage để lưu file và nhận lại tên file đã lưu
            String fileName = uploadService.uploadImage(file);
            newPro.setImg(fileName);
        }

        Long categoryId = productRequestDTO.getCategoryId();
        if (categoryId != null) {
            // Tìm category dựa trên categoryId và gán vào sản phẩm
            Category category = categoryRepository.findById(categoryId).orElse(null);
            newPro.setCategory(category);
        }
        productRepository.save(newPro);
        return new ProductResponseDTO(newPro);
    }
}

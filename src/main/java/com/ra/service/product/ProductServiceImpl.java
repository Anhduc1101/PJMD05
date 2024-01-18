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
import io.micrometer.common.util.StringUtils;
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
        Page<Product> productPage = productRepository.findAllByProductNameContainingIgnoreCase(pageable, name);
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
    public List<ProductResponseDTO> getProductListByCategoryId(Long id) {
        return productRepository.findProductsByCategoryId(id);
    }

    @Override
    public ProductResponseDTO saveOrUpdate(ProductRequestDTO productRequestDTO) throws CustomException {
        Product newPro;
        if (productRepository.existsByProductName(productRequestDTO.getProductName())) {
            throw new CustomException("Product name had been exist");
        }
// check trường hợp trống trường dữ liệu
        if (StringUtils.isBlank(productRequestDTO.getProductName()) || productRequestDTO.getPrice() == null || productRequestDTO.getCategoryId() == null) {
            throw new CustomException("Product name, price, categoryId is required");
        }

        // check trường hợp trống trường file
        MultipartFile file = productRequestDTO.getFile();
        if (file == null || file.isEmpty()) {
            throw new CustomException("Product image is required");
        }
        if (productRequestDTO.getId() == null) {
            newPro = new Product();
            newPro.setProductName(productRequestDTO.getProductName());
            newPro.setStatus(productRequestDTO.getStatus());
            newPro.setPrice(productRequestDTO.getPrice());

//            upload file
            if (productRequestDTO.getFile() != null && productRequestDTO.getFile().getSize() > 0) {
                String fileName = uploadService.uploadImage(productRequestDTO.getFile());
                newPro.setImg(fileName);
            }

//            lay category theo caregoryId tu requestDTO
            Category category = categoryRepository.findById(productRequestDTO.getCategoryId()).orElse(null);

//            set lai category cho product
            newPro.setCategory(category);
            productRepository.save(newPro);
            return new ProductResponseDTO(newPro);

        } else {
            ProductResponseDTO productResponseDTO = findById(productRequestDTO.getId());
            String fileName;
            if (productRequestDTO.getFile() != null && !productRequestDTO.getFile().isEmpty()) {
                fileName = uploadService.uploadImage(productRequestDTO.getFile());
            } else {
                fileName = productResponseDTO.getImg();
            }
            Category category = categoryRepository.findById(productRequestDTO.getCategoryId()).orElse(null);
            Product product = productRepository.save(Product.builder()
                    .id(productResponseDTO.getId())
                    .productName(productRequestDTO.getProductName())
                    .price(productRequestDTO.getPrice())
                    .status(productRequestDTO.getStatus())
                    .img(fileName)
                    .category(category)
                    .build());
            return new ProductResponseDTO(product);
        }
    }
}

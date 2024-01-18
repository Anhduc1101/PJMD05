package com.ra.repository;

import com.ra.model.dto.response.ProductResponseDTO;
import com.ra.model.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Modifying
    @Query("update Product p set p.status=case when p.status=true then false else true end where p.id=?1")
    void changeStatus(Long id);

    Page<Product> findAllByProductNameContainingIgnoreCase(Pageable pageable, String name);
    Boolean existsByProductName(String name);

    @Modifying
    @Query("SELECT p FROM Product p WHERE p.category.id = :id")
    List<ProductResponseDTO> findProductsByCategoryId(@Param("id") Long id);

    Boolean existsProductByCategory_Id(Long id);
}

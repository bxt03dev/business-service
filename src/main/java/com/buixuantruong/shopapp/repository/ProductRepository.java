package com.buixuantruong.shopapp.repository;

import com.buixuantruong.shopapp.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);
    boolean existsByWarrantyCode(String warrantyCode);
    Page<Product> findAll(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.id IN :productIds")
    List<Product> findProductByIds(@Param("productIds") List<Long> productIds);
    
    // Find products by category ID
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
}

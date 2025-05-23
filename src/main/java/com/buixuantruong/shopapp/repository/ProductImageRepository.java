package com.buixuantruong.shopapp.repository;

import com.buixuantruong.shopapp.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    public List<ProductImage> findByProductId(Long productId);
}

package com.buixuantruong.shopapp.services;

import com.buixuantruong.shopapp.dtos.ProductDTO;
import com.buixuantruong.shopapp.dtos.ProductImageDTO;
import com.buixuantruong.shopapp.exception.DataNotFoundException;
import com.buixuantruong.shopapp.exception.InvalidParamException;
import com.buixuantruong.shopapp.models.Product;
import com.buixuantruong.shopapp.models.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws Exception;

    Product getProductById(long id) throws Exception;

    Page<Product> getAllProducts(PageRequest pageRequest);

    Product updateProduct(long id, ProductDTO productDTO) throws Exception;

    void deleteProduct(long id);

    boolean existsProduct(String name);

    ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws DataNotFoundException, InvalidParamException;
}

package com.buixuantruong.shopapp.service;

import com.buixuantruong.shopapp.dto.ProductDTO;
import com.buixuantruong.shopapp.dto.ProductImageDTO;
import com.buixuantruong.shopapp.exception.DataNotFoundException;
import com.buixuantruong.shopapp.exception.InvalidParamException;
import com.buixuantruong.shopapp.model.Product;
import com.buixuantruong.shopapp.model.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws Exception;

    Product getProductById(long id) throws Exception;

    Page<Product> getAllProducts(PageRequest pageRequest);

    Product updateProduct(long id, ProductDTO productDTO) throws Exception;

    void deleteProduct(long id);

    boolean existsProduct(String name);

    ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws DataNotFoundException, InvalidParamException;
}

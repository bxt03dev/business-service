package com.buixuantruong.shopapp.dto.response;

import com.buixuantruong.shopapp.model.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse extends BaseResponse {
    String name;
    float price;
    String thumbnail;
    String description;
    Long categoryId;
    List<MultipartFile> image;

    public static ProductResponse from(Product product) {
        ProductResponse productResponse = ProductResponse.builder()
                .name(product.getName())
                .description(product.getDescription())
                .thumbnail(product.getThumbnail())
                .price(product.getPrice())
                .categoryId(product.getCategory().getId())
                .build();
        productResponse.setCreateAt(product.getCreateAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }
}

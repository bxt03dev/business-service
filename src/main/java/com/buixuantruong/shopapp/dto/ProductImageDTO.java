package com.buixuantruong.shopapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImageDTO {

    @JsonProperty("product_id")
    @Min(value = 1, message = "product's id must be > 0")
    private Long productId;

    @Size(min = 5, max = 255, message = "image's name must be between 5 and 255 characters")
    @JsonProperty("image_url")
    private String imageUrl;
}

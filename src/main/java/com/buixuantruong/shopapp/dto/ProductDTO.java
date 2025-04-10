package com.buixuantruong.shopapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    @NotBlank(message = "title is required")
    @Size(min = 3, max = 200, message = "title must be at least 3 characters")
    private String name;
    @Min(value = 0, message = "price must be higher than or equal to 0")
    private float price;
    private String thumbnail;
    private String description;
    private Long categoryId;
    private List<MultipartFile> image;
}

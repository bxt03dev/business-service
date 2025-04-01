package com.buixuantruong.shopapp.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    @NotBlank(message = "category name can't be empty")
    private String name;
}

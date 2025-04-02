package com.buixuantruong.shopapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value = 1, message = "order's id must be higher 0")
    private Long orderId;

    @JsonProperty("product_id")
    @Min(value = 1, message = "product's id must be higher 0")
    private Long productId;

    @Min(value = 1, message = "price must be higher 0")
    private Long price;

    @JsonProperty("number_of_products")
    @Min(value = 1, message = "number id must be higher 1")
    private int numberOfProducts;

    @JsonProperty("total_money")
    @Min(value = 0, message = "total money must be > 0")
    private int totalMoney;

    private String color;
}

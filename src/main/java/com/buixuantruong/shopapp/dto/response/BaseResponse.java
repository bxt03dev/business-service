package com.buixuantruong.shopapp.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseResponse {
    @JsonProperty("create_at")
    LocalDateTime createAt;

    @JsonProperty("updated_at")
    LocalDateTime updatedAt;
}

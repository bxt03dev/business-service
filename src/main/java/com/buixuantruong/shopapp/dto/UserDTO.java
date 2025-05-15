package com.buixuantruong.shopapp.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    String fullName;
    @NotBlank(message = "phone number is required")
    String phoneNumber;
    String address;
    @NotBlank(message = "password is required")
    String password;
    String retypePassword;
    Date dateOfBirth;
    String email;
    
    // Thông tin đăng nhập xã hội
    String socialProvider; // GOOGLE hoặc FACEBOOK
    String socialProviderId;
    String avatarUrl;
    
    @NotNull(message = "role id is required")
    Long roleId;
}

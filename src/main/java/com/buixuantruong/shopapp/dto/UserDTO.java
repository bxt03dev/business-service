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
    @JsonProperty("fullname")
    String fullName;

    @JsonProperty("phone_number")
    @NotBlank(message = "phone number is required")
    String phoneNumber;

    String address;

    @NotBlank(message = "password is required")
    String password;

    @JsonProperty("retype_password")
    String retypePassword;

    @JsonProperty("date_of_birth")
    Date dateOfBirth;

    @JsonProperty("facebook_account_id")
    String facebookAccountId;

    @JsonProperty("google_account_id")
    String googleAccountId;

    @NotNull(message = "role id is required")
    @JsonProperty("role_id")
    Long roleId;
}

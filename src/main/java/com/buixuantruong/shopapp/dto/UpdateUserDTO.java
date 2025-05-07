package com.buixuantruong.shopapp.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserDTO {
    String fullName;
    String phoneNumber;
    String address;
    String password;
    String retypePassword;
    Date dateOfBirth;
    Long facebookAccountId;
    Long googleAccountId;
}

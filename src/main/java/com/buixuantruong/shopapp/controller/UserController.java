package com.buixuantruong.shopapp.controller;

import com.buixuantruong.shopapp.dto.*;
import com.buixuantruong.shopapp.dto.response.ApiResponse;
import com.buixuantruong.shopapp.exception.DataNotFoundException;
import com.buixuantruong.shopapp.exception.StatusCode;
import com.buixuantruong.shopapp.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userServiceImpl;
    @PostMapping("/register")
    public ApiResponse<Object> register(@Valid @RequestBody UserDTO userDTO,
                                        BindingResult bindingResult) {
        try{
            if(bindingResult.hasErrors()) {
                List<String> errorMessages = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ApiResponse.builder()
                        .message(String.join(", ", errorMessages))
                        .build();
            }
            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                return ApiResponse.builder()
                        .message("password and retype password are not the same")
                        .build();
            }
            return userServiceImpl.createUser(userDTO);
        }
        catch(Exception e){
            return ApiResponse.builder()
                    .message(e.getMessage())
                    .build();
        }
    }

    @PostMapping("/login")
    public ApiResponse<Object> login(@Valid @RequestBody UserLoginDTO userLoginDTO) throws DataNotFoundException {
        String token = userServiceImpl.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword());
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(StatusCode.SUCCESS.getMessage())
                .result(token)
                .build();
    }
}

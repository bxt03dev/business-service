package com.buixuantruong.shopapp.controller;

import com.buixuantruong.shopapp.dto.*;
import com.buixuantruong.shopapp.dto.response.ApiResponse;
import com.buixuantruong.shopapp.service.UserService;
import jakarta.validation.Valid;
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
            userServiceImpl.createUser(userDTO);
            return ApiResponse.builder()
                    .message("user created successfully")
                    .build();
        }
        catch(Exception e){
            return ApiResponse.builder()
                    .message(e.getMessage())
                    .build();
        }
    }

    @PostMapping("/login")
    public ApiResponse<Object> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {

        return userServiceImpl.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword());
    }
}

package com.buixuantruong.shopapp.controller;

import com.buixuantruong.shopapp.dto.*;
import com.buixuantruong.shopapp.dto.response.ApiResponse;
import com.buixuantruong.shopapp.dto.response.UserResponse;
import com.buixuantruong.shopapp.exception.DataNotFoundException;
import com.buixuantruong.shopapp.exception.StatusCode;
import com.buixuantruong.shopapp.model.User;
import com.buixuantruong.shopapp.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
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
            return userService.createUser(userDTO);
        }
        catch(Exception e){
            return ApiResponse.builder()
                    .message(e.getMessage())
                    .build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        try {
            String token = userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword());
            ApiResponse<Object> response = ApiResponse.builder()
                    .code(StatusCode.SUCCESS.getCode())
                    .message(StatusCode.SUCCESS.getMessage())
                    .result(token)
                    .build();
            return ResponseEntity.ok(response);
        } catch (DataNotFoundException e) {
            ApiResponse<Object> errorResponse = ApiResponse.builder()
                    .code(StatusCode.INVALID_CREDENTIALS.getCode()) // Ví dụ: 1001
                    .message(e.getMessage())
                    .result(null)
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @PostMapping("/details")
    public ApiResponse<Object> getUserDetails(@RequestHeader("Authorization") String token) throws Exception {
        String extractedToken = token.substring(7);
        User user = userService.getUserDetailByToken(extractedToken);
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(StatusCode.SUCCESS.getMessage())
                .result(UserResponse.fromUser(user))
                .build();
    }
}

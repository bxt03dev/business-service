package com.buixuantruong.shopapp.service;

import com.buixuantruong.shopapp.dto.ApiResponse;
import com.buixuantruong.shopapp.dto.UserDTO;
import com.buixuantruong.shopapp.exception.DataNotFoundException;
import com.buixuantruong.shopapp.model.User;
import org.springframework.stereotype.Service;

@Service
public interface  UserService {
    ApiResponse<Object> createUser(UserDTO userDTO) throws DataNotFoundException;

    ApiResponse<Object> login(String phoneNumber, String password);
}

package com.buixuantruong.shopapp.service;

import com.buixuantruong.shopapp.dto.response.ApiResponse;
import com.buixuantruong.shopapp.dto.UserDTO;
import com.buixuantruong.shopapp.exception.DataNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface  UserService {
    ApiResponse<Object> createUser(UserDTO userDTO) throws Exception;

    String login(String phoneNumber, String password) throws DataNotFoundException;
}

package com.buixuantruong.shopapp.service;

import com.buixuantruong.shopapp.dto.UserDTO;
import com.buixuantruong.shopapp.exception.DataNotFoundException;
import com.buixuantruong.shopapp.model.User;
import org.springframework.stereotype.Service;

@Service
public interface  UserService {
    User createUser(UserDTO userDTO) throws DataNotFoundException;

    String login(String phoneNumber, String password);
}

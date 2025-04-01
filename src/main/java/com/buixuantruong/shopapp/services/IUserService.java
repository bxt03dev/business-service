package com.buixuantruong.shopapp.services;

import com.buixuantruong.shopapp.dtos.UserDTO;
import com.buixuantruong.shopapp.exception.DataNotFoundException;
import com.buixuantruong.shopapp.models.User;
import org.springframework.stereotype.Service;

@Service
public interface IUserService {
    User createUser(UserDTO userDTO) throws DataNotFoundException;

    String login(String phoneNumber, String password);
}

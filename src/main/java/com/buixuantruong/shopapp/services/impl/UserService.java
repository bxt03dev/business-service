package com.buixuantruong.shopapp.services.impl;

import com.buixuantruong.shopapp.dtos.UserDTO;
import com.buixuantruong.shopapp.exception.DataNotFoundException;
import com.buixuantruong.shopapp.models.Role;
import com.buixuantruong.shopapp.models.User;
import com.buixuantruong.shopapp.repositories.RoleRepository;
import com.buixuantruong.shopapp.repositories.UserRepository;
import com.buixuantruong.shopapp.services.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements IUserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    @Override
    public User createUser(UserDTO userDTO) throws DataNotFoundException {
        String phoneNumber = userDTO.getPhoneNumber();
        if(userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .password(userDTO.getPassword())
                .phoneNumber(userDTO.getPhoneNumber())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .build();
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        newUser.setRole(role);

        if(userDTO.getFacebookAccountId().length() == 0 && userDTO.getGoogleAccountId().length() == 0){
            String password = userDTO.getPassword();
            //String encodedPassword = passwordEncouder.encode(password);
            //newUser.setPassword(encodedPassword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) {

        return null;
    }
}

package com.example.decode.service.Interface;

import com.example.decode.dto.UserDto;
import com.example.decode.entity.UserEntity;

public interface UserInterface {
    UserEntity saveUser(UserEntity userDto);

    UserEntity viewUser(String userName);

    boolean isUsernameExists(String userName);

    UserEntity updateUser(Long id, UserEntity userDetails);

    void deleteUser(Long id);
}


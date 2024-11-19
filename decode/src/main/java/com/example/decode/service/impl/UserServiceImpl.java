package com.example.decode.service.impl;

import com.example.decode.entity.UserEntity;
import com.example.decode.repository.UserRepo;
import com.example.decode.service.Interface.UserInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserInterface {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserEntity saveUser(UserEntity user) {
        return userRepo.save(user);
    }

//    @Override
//    public UserEntity viewUser(String userName) {
//        return userRepo.findByUserName(userName);
//    }

    @Override
    public boolean isUsernameExists(String userName) {
        return userRepo.existsByUserName(userName);
    }

    @Override
    public UserEntity updateUser(Long id, UserEntity userDetails) {
        UserEntity user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setUserName(userDetails.getUserName());
        user.setEmailId(userDetails.getEmailId());
        user.setAddress(userDetails.getAddress());

        // Update image data if available
        if (userDetails.getProfileImage() != null && userDetails.getProfileImage().length > 0) {
            user.setProfileImage(userDetails.getProfileImage());  // Update image data
        }

        return userRepo.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        UserEntity user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        if (user != null) {
            userRepo.deleteById(id);
        }
    }
    @Transactional(readOnly = true)
    @Override
    public UserEntity viewUser(String userName) {
        List<UserEntity> users = userRepo.findByUserName(userName);
        if (users.isEmpty()) {
            throw new RuntimeException("User not found with username: " + userName);
        } else if (users.size() > 1) {
            throw new RuntimeException("Multiple users found with username: " + userName);
        }
        return users.get(0);
    }
}

package com.example.decode.repository;

import com.example.decode.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<UserEntity,Long> {
//    UserEntity findByUserName(String userName);
List<UserEntity> findByUserName(String userName);
    boolean existsByUserName(String userName);
}

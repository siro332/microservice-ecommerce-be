package com.example.userservice.repositories;

import com.example.userservice.models.entities.UserInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInfoRepository extends MongoRepository<UserInfo,String> {
    List<UserInfo> findAll();
    Optional<UserInfo> findByCode(String code);
}

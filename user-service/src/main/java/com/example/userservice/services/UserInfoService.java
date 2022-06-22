package com.example.userservice.services;

import com.example.userservice.models.entities.UserInfo;
import com.example.userservice.repositories.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserInfoService {
    private final UserInfoRepository userInfoRepository;

    public List<UserInfo> getAllUserInfo() {
        return userInfoRepository.findAll();
    }

    public UserInfo getUserInfoByCode(String code) {
        try {
            return userInfoRepository.findByCode(code).get();
        }catch (Exception e){
            return userInfoRepository.save(UserInfo.builder().code(code)
                            .address("")
                            .city("")
                            .district("")
                            .email("")
                            .name("")
                            .phoneNumber("")
                            .address("")
                            .zipcode("")
                    .build());
        }
    }

    public UserInfo addUserInfo(UserInfo userInfo) {
        Optional<UserInfo> optionalUserInfo = userInfoRepository.findByCode(userInfo.getCode());
        if (optionalUserInfo.isPresent()){
            UserInfo oldUserInfo = optionalUserInfo.get();
            userInfo.setId(oldUserInfo.getId());
        }
        return userInfoRepository.save(userInfo);
    }
}

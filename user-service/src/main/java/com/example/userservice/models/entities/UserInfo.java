package com.example.userservice.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("userInfo")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    @Id
    private String id;
    private String code;
    private String name;
    private String address;
    private String city;
    private String district;
    private String zipcode;
    private String phoneNumber;
    private String email;
    private Date createdAt;
    private Date updatedAt;
}

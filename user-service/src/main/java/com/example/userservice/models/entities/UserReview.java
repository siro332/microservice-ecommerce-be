package com.example.userservice.models.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("userReview")
@Data
public class UserReview {
    @Id
    private String id;
    private String code;
    private String productCode;
    private String userCode;
    private Integer rate;
    private String review;
    private Date createdAt;
    private Date updatedAt;
}

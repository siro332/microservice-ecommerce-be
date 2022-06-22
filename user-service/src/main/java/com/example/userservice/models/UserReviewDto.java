package com.example.userservice.models;

import com.example.userservice.models.entities.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserReviewDto {
    private String id;
    private String code;
    private String productCode;
    private Integer rate;
    private String review;
    private Date createdAt;
    private Date updatedAt;
    private UserInfo userInfo;
}

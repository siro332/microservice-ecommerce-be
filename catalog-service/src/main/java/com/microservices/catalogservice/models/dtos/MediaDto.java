package com.microservices.catalogservice.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaDto {
    private String code;
    private String imgUrl;
    private String altText;
    private Boolean isFeature;
    private Date createdAt;
    private Date updatedAt;
}

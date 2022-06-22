package com.microservices.favoriteservice.models.catalog;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class CategoryDto {
    private String code;
    private String name;
    private String slug;
    private String parentCode;
    private Boolean isActive;
    private Date createdAt;
    private Date updatedAt;
    private List<CategoryDto> subCategories;
    private List<MediaDto> mediaList;
}


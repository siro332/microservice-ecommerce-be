package com.example.searchservice.models.catalog;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class ProductDto {
    private String code;
    private String name;
    private String slug;
    private Boolean isActive;
    private Date createdAt;
    private Date updatedAt;
    private String userCode;
    private String description;
    private Set<CategoryDto> categories;
    private List<MediaDto> mediaList;
}

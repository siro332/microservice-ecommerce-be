package com.microservices.catalogservice.models.dtos;

import com.microservices.catalogservice.models.entities.product_inventory.ProductInventory;
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
    private List<ProductInventory> inventoryList;
    private long totalUnitSold;
}

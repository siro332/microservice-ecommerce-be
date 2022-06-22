package com.microservices.favoriteservice.models.product_inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Discount {

    private Long id;
    private String code;
    private String name;
    private String description;
    private Double discountPercent;
    private Boolean isActive;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
}


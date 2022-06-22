package com.microservices.favoriteservice.models.product_inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttribute {

    private Long id;
    private String code;
    private String name;
    private String description;
}

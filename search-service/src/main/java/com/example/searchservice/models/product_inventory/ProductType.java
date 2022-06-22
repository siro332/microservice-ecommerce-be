package com.example.searchservice.models.product_inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductType {
    private Long id;
    private String code;
    private String name;
    private Set<ProductAttribute> productAttributes;
}

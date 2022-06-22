package com.example.searchservice.models.product_inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttributeValue {
    private Long id;
    private ProductAttribute productAttribute;
    private String code;
    private String attributeValue;
}

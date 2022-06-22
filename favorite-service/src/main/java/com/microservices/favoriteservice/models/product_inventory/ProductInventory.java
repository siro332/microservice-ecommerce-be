package com.microservices.favoriteservice.models.product_inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInventory {
    private Long id;
    private String sku;
    //universal product code
    private String upc;
    private ProductType productType;
    private String productCode;
    private Brand brand;
    private Discount discount;
    private Set<ProductAttributeValue> productAttributeValues;
    private Boolean isActive;
    private Double retailPrice;
    private Date createdAt;
    private Date updatedAt;
}
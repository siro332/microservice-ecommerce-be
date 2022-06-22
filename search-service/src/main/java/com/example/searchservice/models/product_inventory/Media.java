package com.example.searchservice.models.product_inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Media {

    private Long id;
    private String code;
    private ProductInventory productInventory;
    private String imgUrl;
    private String altText;
    private Boolean isFeature;
    private Date createdAt;
    private Date updatedAt;
}

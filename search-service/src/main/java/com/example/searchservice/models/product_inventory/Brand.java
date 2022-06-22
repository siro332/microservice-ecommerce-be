package com.example.searchservice.models.product_inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Brand {
    private Long id;
    private String code;
    private String name;
    private Date createdAt;
}

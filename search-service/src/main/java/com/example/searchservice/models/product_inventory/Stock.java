package com.example.searchservice.models.product_inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    private Long id;
    private Date lastChecked;
    private Long units;
    private Long unitsSold;
}

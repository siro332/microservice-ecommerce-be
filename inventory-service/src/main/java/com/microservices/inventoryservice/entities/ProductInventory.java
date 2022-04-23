package com.microservices.inventoryservice.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "product_inventories")
public class ProductInventory {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String sku;
    @Column(nullable = false, unique = true)
    //universal product code
    private String upc;
    @ManyToOne
    @JoinColumn(name = "product_type_id")
    private ProductType productType;
    private String productCode;
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;
    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;
    @ManyToMany
    private Set<ProductAttributeValue> productAttributeValues;
    private Boolean isActive;
    private Double retailPrice;
    private Date createdAt;
    private Date updatedAt;
}
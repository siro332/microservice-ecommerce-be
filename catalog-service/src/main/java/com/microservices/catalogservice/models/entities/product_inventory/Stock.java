package com.microservices.catalogservice.models.entities.product_inventory;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "stock")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    @JoinColumn(name = "product_inventory_id")
    private ProductInventory productInventory;
    private Date lastChecked;
    private Long units;
    private Long unitsSold;
}

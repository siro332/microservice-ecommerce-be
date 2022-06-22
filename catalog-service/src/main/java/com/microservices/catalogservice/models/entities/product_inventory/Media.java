package com.microservices.catalogservice.models.entities.product_inventory;

import lombok.Data;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "media")
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String code;
    @ManyToOne
    @JoinColumn(name = "product_inventory_id")
    private ProductInventory productInventory;
    private String imgUrl;
    @Nationalized
    private String altText;
    private Boolean isFeature;
    private Date createdAt;
    private Date updatedAt;
}

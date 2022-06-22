package com.microservices.catalogservice.models.entities.product_inventory;

import lombok.Data;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;

@Data
@Entity
@Table(name = "product_attribute_values")
public class ProductAttributeValue {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "product_attribute_id")
    private ProductAttribute productAttribute;
    @Column(nullable = false, unique = true)
    private String code;
    @Nationalized
    private String attributeValue;
}

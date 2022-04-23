package com.microservices.catalogservice.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String code;
    @Column(nullable = false)
    @Nationalized
    private String name;
    private String slug;
    private Boolean isActive;
    private Date createdAt;
    private Date updatedAt;
    private String userCode;
    @Column(columnDefinition = "text")
    private String description;
    @ManyToMany
    private Set<Category> categories;

}
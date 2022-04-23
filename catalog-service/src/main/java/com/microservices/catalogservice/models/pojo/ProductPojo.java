package com.microservices.catalogservice.models.pojo;

import com.microservices.catalogservice.models.entities.Category;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import javax.persistence.Column;
import javax.persistence.ManyToMany;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
public class ProductPojo {
    private String code;
    private String name;
    private String slug;
    private Boolean isActive;
    private Date createdAt;
    private Date updatedAt;
    private String userCode;
    private String description;
    private Set<String> categoryCodeSet;
}

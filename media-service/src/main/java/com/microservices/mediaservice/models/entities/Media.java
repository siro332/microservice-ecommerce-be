package com.microservices.mediaservice.models.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("mediaItems")
@Data
public class Media {
    @Id
    private String id;
    private String code;
    private String type;
    private String imgUrl;
    private String altText;
    private Boolean isFeature;
    private Date createdAt;
    private Date updatedAt;
}

package com.microservices.mediaservice.repositories;

import com.microservices.mediaservice.models.entities.Media;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaRepository extends MongoRepository<Media,String> {
    List<Media> findAll();
    List<Media> findByCode(String code);
    List<Media> findByType(String type);
}

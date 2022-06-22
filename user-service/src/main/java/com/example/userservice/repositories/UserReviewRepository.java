package com.example.userservice.repositories;

import com.example.userservice.models.entities.UserReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserReviewRepository extends MongoRepository<UserReview,String> {
    List<UserReview> findAllByProductCode(String code);
    Page<UserReview> findByProductCode(String code, Pageable pageable);
}

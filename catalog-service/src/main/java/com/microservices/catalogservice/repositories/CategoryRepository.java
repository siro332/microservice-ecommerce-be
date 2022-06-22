package com.microservices.catalogservice.repositories;

import com.microservices.catalogservice.models.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Optional<Category> findByCode(String code);
    List<Category> findAll();
    Set<Category> findByCodeIn(Set<String> categoryCodeList);
    List<Category> findAllByParentCodeIsNull();
    List<Category> findAllByParentCodeIsNotNull();
    List<Category> findAllByCodeIn(List<String> codeList);
}

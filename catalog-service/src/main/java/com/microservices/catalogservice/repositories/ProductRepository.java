package com.microservices.catalogservice.repositories;

import com.microservices.catalogservice.models.entities.Category;
import com.microservices.catalogservice.models.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findByCode(String code);
    Page<Product> findAll(Pageable pageable);
    Page<Product> findByUserCode(String userCode, Pageable pageable);
    Page<Product> findByCategoriesContains(Category category,Pageable pageable);
    List<Product> findByCategoriesContains(Category category);

}
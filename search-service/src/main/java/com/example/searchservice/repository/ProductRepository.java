package com.example.searchservice.repository;

import com.example.searchservice.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ElasticsearchRepository<Product,String> {
    @Override
    Page<Product> findAll(Pageable pageable);
    Product findProductByCode(String code);
    Page<Product> findProductByAllContaining(String searchParam,Pageable pageable);
    Page<Product> findProductByCategoriesCodeContains(String code,Pageable pageable);
    Page<Product> findProductByAllContainingAndCategoriesCodeContainsAndPriceBetweenAndBrandNameContaining(
            String productName, String categoryCode, double priceStart, double priceEnd, String brandName,Pageable pageable);
    Page<Product> findProductByAllContainingAndPriceBetweenAndBrandNameContaining(
            String productName, double priceStart, double priceEnd, String brandName,Pageable pageable);

}

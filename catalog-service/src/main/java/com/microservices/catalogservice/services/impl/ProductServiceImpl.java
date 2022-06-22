package com.microservices.catalogservice.services.impl;

import com.microservices.catalogservice.common.Utils;
import com.microservices.catalogservice.converters.DtoConverter;
import com.microservices.catalogservice.models.dtos.MediaDto;
import com.microservices.catalogservice.models.dtos.ProductDto;
import com.microservices.catalogservice.models.entities.Category;
import com.microservices.catalogservice.models.entities.Product;
import com.microservices.catalogservice.models.entities.product_inventory.ProductInventory;
import com.microservices.catalogservice.models.entities.product_inventory.Stock;
import com.microservices.catalogservice.models.pojo.ProductPojo;
import com.microservices.catalogservice.repositories.CategoryRepository;
import com.microservices.catalogservice.repositories.ProductRepository;
import com.microservices.catalogservice.services.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryServiceImpl categoryService;
    private final MediaServiceClientImpl mediaServiceClient;
    private final ProductInventoryClientImpl productInventoryClient;
    private final KafkaTemplate<String,String> kafkaTemplate;
    private final DtoConverter dtoConverter;
    @Override
    public Product createProduct(ProductPojo form) {
        try {
            Set<Category> categories = categoryService.getListCategoryByCode(form.getCategoryCodeSet());
            UUID uuid = UUID.randomUUID();
            Product newProduct = Product.builder()
                    .name(form.getName())
                    .code("PD" + Utils.uuidToBase64(uuid))
                    .slug(form.getSlug())
                    .isActive(form.getIsActive())
                    .createdAt(new Date())
                    .description(form.getDescription())
                    .userCode(form.getUserCode())
                    .categories(categories).build();
            log.info(newProduct.toString());
            sendToKafka(newProduct.getCode());
            return productRepository.save(newProduct);
        } catch (Exception e) {
            log.error("Error while creating product");
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Product updateProduct(ProductPojo form) {
        try {
            Set<Category> categories = categoryService.getListCategoryByCode(form.getCategoryCodeSet());
            Optional<Product> optionalProduct = productRepository.findByCode(form.getCode());
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                product.setCategories(categories);
                product.setUpdatedAt(new Date());
                product.setIsActive(form.getIsActive());
                product.setName(form.getName());
                product.setSlug(form.getSlug());
                product.setDescription(form.getDescription());
                sendToKafka(product.getCode());
                return productRepository.save(product);
            }else{
                log.error("Product with code: "+form.getCode()+" not found!");
                throw new Exception();
            }
        } catch (Exception e) {
            log.error("Error while creating product");
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Page<ProductDto> getUserProducts(String userCode, Pageable paging) {
        Page<Product> productPage = productRepository.findByUserCode(userCode,paging);
        return productPage.map(this::getProductDetails);
    }

    @Override
    public Page<ProductDto> getAllProducts(Pageable paging) {
        Page<Product> productPage = productRepository.findAll(paging);
        return productPage.map(this::getProductDetails);
    }

    @Override
    public Page<ProductDto> getProductContain(Pageable pageable, Specification<Product> spec) {
        Page<Product> productPage = productRepository.findAll(spec,pageable);
        return productPage.map(this::getProductDetails);
    }

    @Override
    public Optional<ProductDto> getProductByCode(String code) {
       Optional<Product> optionalProduct = productRepository.findByCode(code);
       if (optionalProduct.isPresent()) {
           log.info("Fetching media for product_code: " + code);
           Product product = optionalProduct.get();
           ProductDto productDto = getProductDetails(product);
           return Optional.of(productDto);
       }
       return Optional.empty();
    }

    @Override
    public void deleteProductByCode(String code) {
        try {
            Optional<Product> optionalProduct = productRepository.findByCode(code);
            if (optionalProduct.isPresent()) {
                optionalProduct.get().setIsActive(false);
                sendToKafka(optionalProduct.get().getCode());
                productRepository.save(optionalProduct.get());
            }else{
                log.error("Product with code: "+code+" not found!");
                throw new Exception();
            }
        } catch (Exception e) {
            log.error("Error while delete product");
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public Page<ProductDto> getProductByCategoryCode(String categoryCode, Pageable paging) {
        try {
            Optional<Category> optionalCategory = categoryRepository.findByCode(categoryCode);
            if (optionalCategory.isPresent()){
                Page<Product> productPage = productRepository.findByCategoriesContains(optionalCategory.get(),paging);
                return productPage.map(this::getProductDetails);
            }
        }catch (Exception e){
            log.error("Error finding category with code: "+categoryCode);
            return Page.empty();
        }
        return null;
    }
    public Page<ProductDto> getProductCodeByCategoryCode(String categoryCode, Pageable pageable) {
        try {
            Optional<Category> optionalCategory = categoryRepository.findByCode(categoryCode);
            if (optionalCategory.isPresent()){
                List<ProductDto> productPage = productRepository.findByCategoriesContains(optionalCategory.get()).stream().map(this::getProductDetails).collect(Collectors.toList());
                productPage.sort(Comparator.comparing(productDto -> productDto.getTotalUnitSold(),Comparator.reverseOrder()));
                final int start = (int)pageable.getOffset();
                final int end = Math.min((start + pageable.getPageSize()), productPage.size());
                final Page<ProductDto> page = new PageImpl<>(productPage.subList(start, end), pageable, productPage.size());
                return page;
            }

        }catch (Exception e){
            log.error("Error finding category with code: "+categoryCode);
        }
        return null;
    }

    private ProductDto getProductDetails(Product product) {
            List<MediaDto> mediaList = mediaServiceClient.getMediaByProductCode(product.getCode()).get();
            List<ProductInventory> productInventories = productInventoryClient.getProductInventoryByProductCode(product.getCode()).get();
            ProductDto productDto = dtoConverter.productEntityToDto(product);
            productDto.setMediaList(mediaList);
            productDto.setInventoryList(productInventories);
            productDto.setTotalUnitSold(productInventories.stream().mapToLong(productInventory -> productInventory.getStock().getUnitsSold()).sum());
            return productDto;
    }
    private void  sendToKafka(String message){
        kafkaTemplate.send("product",message);
        kafkaTemplate.flush();
    }
}
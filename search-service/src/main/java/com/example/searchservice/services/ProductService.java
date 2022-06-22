package com.example.searchservice.services;

import com.example.searchservice.models.Product;
import com.example.searchservice.models.catalog.CategoryDto;
import com.example.searchservice.models.catalog.ProductDto;
import com.example.searchservice.models.product_inventory.ProductAttributeValue;
import com.example.searchservice.models.product_inventory.ProductInventory;
import com.example.searchservice.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductClientImpl productClient;

    private final ProductInventoryClientImpl productInventoryClient;
    private Set<String> productIdSet = new HashSet<>( );

    private ObjectMapper mapper = new ObjectMapper();

    @Scheduled(cron = "0 0/1 * ? * * ")
    public void scheduleAddProducts() throws IOException {
        log.info("scheduling started");
        Set<String> productIds;
        synchronized (productIdSet){
            productIds = productIdSet;
            productIdSet = new HashSet<>();
            addProducts(productIds);
        }
    }

    public void addProducts(Set<String> productIdSet) throws IOException {
        for (String productId :
                productIdSet) {
            buildProduct(productId);
        }
    }


    public Product buildProduct(String productCode) throws IOException {
        Product product = new Product();
        product.setCode(productCode);
        //Query product category name
        ProductDto productDto = productClient.getProductByCode(productCode).get();
        List<String> names = productDto.getCategories().stream().map(CategoryDto::getName).collect(Collectors.toList());
        List<String> categoriesCode = productDto.getCategories().stream().map(CategoryDto::getCode).collect(Collectors.toList());
        //query sku
        List<ProductInventory> skus = this.productInventoryClient.getProductInventoryByProductCode(productDto.getCode());
        //Process sku, only encapsulate id, price, title, image, and get price collection
        List<Double> prices = new ArrayList<>();
        AtomicLong sales = new AtomicLong();
        List<Map<String, Object>> skuList = new ArrayList<>();
        skus.forEach(sku -> {
            List<String> attributeName  = sku.getProductAttributeValues().stream().map(ProductAttributeValue::getAttributeValue).collect(Collectors.toList());
            prices.add(sku.getRetailPrice());
            sales.addAndGet(sku.getStock().getUnitsSold());
            Map<String, Object> skuMap = new HashMap<>();
            skuMap.put("code", sku.getSku());
            skuMap.put("title", attributeName.toString().replace("[","").replace("]",""));
            skuMap.put("price", sku.getRetailPrice());
            skuList.add(skuMap);
        });

        Map<String,List<String>> specs = new HashMap<>();
        //Processing specification parameter
        for (ProductInventory sku: skus
             ) {
            sku.getProductAttributeValues().stream().forEach(productAttributeValue -> {
                if(!specs.containsKey(productAttributeValue.getProductAttribute().getName())){
                    specs.put(productAttributeValue.getProductAttribute().getName(),new ArrayList<>());
                }
                specs.get(productAttributeValue.getProductAttribute().getName()).add(productAttributeValue.getAttributeValue());
            });
        }

        product.setCode(productCode);
        product.setName(productDto.getName());
        product.setCategoriesCode(categoriesCode);
        product.setBrandName(skus.stream().map(productInventory -> productInventory.getBrand().getName()).collect(Collectors.toList()));
        product.setCreateTime(productDto.getCreatedAt());
        product.setAll(productDto.getName() + " " + StringUtils.join(names, " "));
        product.setPrice(prices.stream().mapToDouble(Double::doubleValue).average().orElse(0));
        product.setImg(productDto.getMediaList().size()>0?productDto.getMediaList().get(0).getImgUrl():"");
        product.setSkus(mapper.writeValueAsString(skuList));
        product.setSpecs(specs);
        product.setSales(sales.get());
        product.setRating(0);
        log.debug(product.toString());
        productRepository.save(product);
        return product;
    }
    public void deleteProduct(String productCode){
        productRepository.delete(productRepository.findProductByCode(productCode));
    }
    public Page<Product> getAllProducts(Pageable paging) {
        Page<Product> productPage = productRepository.findAll(paging);
        return productPage;
    }
    public Page<Product> getProductByCategoryCode(String code,Pageable paging) {
        Page<Product> productPage = productRepository.findProductByCategoriesCodeContains(code,paging);
        return productPage;
    }

    public Set<String> getProductIdSet() {
        synchronized (productIdSet){
            return productIdSet;
        }
    }

    public void setProductIdSet(Set<String> productIdSet) {
        this.productIdSet = productIdSet;
    }

    public void addKafkaData(String data) {
        productIdSet.add(data);
    }
}

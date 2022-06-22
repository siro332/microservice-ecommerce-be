package com.example.searchservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

//
@Document(indexName = "product")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    @Id
    @Field
    private String code;//spuId
    @Field(type = FieldType.Text,fielddata = true)
    private String name;
    @Field(type = FieldType.Text,fielddata = true)
    private String all;//All the information that needs to be searched, including title, category, and even brand
    private List<String> brandName;//brand id
    private List<String> categoriesCode;
    private Date createTime;//Creation time
    private double price;//price
    private String img;
    @Field(type = FieldType.Keyword, index = false)
    private String skus;//json structure of sku information
    private Map<String, List<String>> specs;//Searchable specification parameters, the key is the parameter name, and the value is the parameter value
    private long sales;
    private double rating;

}
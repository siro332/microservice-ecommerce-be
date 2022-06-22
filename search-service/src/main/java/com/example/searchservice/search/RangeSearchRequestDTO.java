package com.example.searchservice.search;

import lombok.Data;

@Data
public class RangeSearchRequestDTO {
    String field;
    double from;
    double to;
}

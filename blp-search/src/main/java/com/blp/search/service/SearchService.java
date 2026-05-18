package com.blp.search.service;

import com.blp.search.entity.ProductDocument;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface SearchService {
    void fullSync();
    Map<String, Object> search(String keyword, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, int page, int size);
}

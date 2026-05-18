package com.blp.search.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.json.JsonData;
import com.blp.search.entity.ProductDocument;
import com.blp.search.mapper.ProductSearchMapper;
import com.blp.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final ElasticsearchClient esClient;
    private final ProductSearchMapper productSearchMapper;
    private static final String INDEX_NAME = "products";

    @Override
    public void fullSync() {
        try {
            // Check if index exists, delete and recreate
            ExistsRequest existsRequest = ExistsRequest.of(e -> e.index(INDEX_NAME));
            if (esClient.indices().exists(existsRequest).value()) {
                esClient.indices().delete(d -> d.index(INDEX_NAME));
            }
            esClient.indices().create(c -> c.index(INDEX_NAME));

            List<ProductDocument> products = productSearchMapper.findAllProducts();
            if (products.isEmpty()) {
                log.info("No products to sync to ES");
                return;
            }

            // Bulk index
            List<BulkOperation> operations = products.stream().map(product ->
                    BulkOperation.of(op -> op.index(idx -> idx
                            .index(INDEX_NAME)
                            .id(String.valueOf(product.getId()))
                            .document(product)))
            ).toList();

            esClient.bulk(b -> b.operations(operations));
            log.info("Synced {} products to ES", products.size());
        } catch (Exception e) {
            log.error("Failed to sync products to ES", e);
            throw new RuntimeException("ES sync failed: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> search(String keyword, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, int page, int size) {
        try {
            BoolQuery.Builder boolBuilder = new BoolQuery.Builder();

            if (keyword != null && !keyword.isBlank()) {
                boolBuilder.must(Query.of(q -> q.multiMatch(mm -> mm
                        .fields("name", "description", "categoryName", "brandName")
                        .query(keyword))));
            }

            if (categoryId != null) {
                boolBuilder.filter(Query.of(q -> q.term(t -> t
                        .field("categoryId")
                        .value(categoryId))));
            }

            if (minPrice != null || maxPrice != null) {
                RangeQuery.Builder rangeBuilder = new RangeQuery.Builder().field("price");
                if (minPrice != null) {
                    rangeBuilder.gte(JsonData.of(minPrice));
                }
                if (maxPrice != null) {
                    rangeBuilder.lte(JsonData.of(maxPrice));
                }
                boolBuilder.filter(Query.of(q -> q.range(rangeBuilder.build())));
            }

            SearchRequest searchRequest = SearchRequest.of(s -> s
                    .index(INDEX_NAME)
                    .from((page - 1) * size)
                    .size(size)
                    .query(q -> q.bool(boolBuilder.build())));

            SearchResponse<ProductDocument> response = esClient.search(searchRequest, ProductDocument.class);

            List<ProductDocument> records = response.hits().hits().stream()
                    .map(Hit::source)
                    .filter(Objects::nonNull)
                    .toList();

            Map<String, Object> result = new HashMap<>();
            result.put("total", response.hits().total() != null ? response.hits().total().value() : 0);
            result.put("page", page);
            result.put("size", size);
            result.put("records", records);
            return result;
        } catch (Exception e) {
            log.error("Search failed", e);
            throw new RuntimeException("Search failed: " + e.getMessage(), e);
        }
    }
}

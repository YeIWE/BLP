package com.blp.search.mapper;

import com.blp.search.entity.ProductDocument;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductSearchMapper {

    @Select("SELECT p.id, p.name, p.category_id as categoryId, c.name as categoryName, b.name as brandName, " +
            "p.price, p.description, p.create_time as createTime " +
            "FROM pms_product p " +
            "LEFT JOIN pms_category c ON p.category_id = c.id " +
            "LEFT JOIN pms_brand b ON p.brand_id = b.id " +
            "WHERE p.status = 1")
    List<ProductDocument> findAllProducts();
}

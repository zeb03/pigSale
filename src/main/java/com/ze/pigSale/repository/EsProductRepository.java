package com.ze.pigSale.repository;

import com.ze.pigSale.entity.EsProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.CompletionContext;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author zeb
 * @Date 2023-08-07 15:31
 * 商品ES操作类
 */
public interface EsProductRepository extends ElasticsearchRepository<EsProduct, Long> {
    /**
     * 简单搜索
     * @param productName
     * @param categoryName
     * @param description
     * @param page
     * @return
     */
    Page<EsProduct> findByProductNameOrCategoryNameOrDescription(String productName, String categoryName, String description, Pageable page);
}

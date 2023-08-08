package com.ze.pigSale.service;

import com.ze.pigSale.common.Result;
import com.ze.pigSale.entity.EsProduct;
import com.ze.pigSale.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author zeb
 * @Date 2023-08-07 15:32
 */
public interface EsProductService {

    /**
     * 从数据库中导入所有商品到ES
     */
    int importAll();

    /**
     * 根据id删除商品
     */
    void delete(Long id);

    /**
     * 根据id创建商品
     */
    EsProduct create(Long id);

    /**
     * 批量删除商品
     */
    void delete(List<Long> ids);

    /**
     * 根据关键字搜索名称或者副标题
     */
    Page<EsProduct> search(String keyword, Integer pageNum, Integer pageSize);

    public EsProduct getEsProduct(Product item);

    Result<List> suggest(String prefix);

    Result<Map<String, List>> aggregate(String keyword, Integer pageNum, Integer pageSize);
}

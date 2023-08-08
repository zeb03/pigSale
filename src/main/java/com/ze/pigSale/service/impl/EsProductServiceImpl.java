package com.ze.pigSale.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.ze.pigSale.common.CustomException;
import com.ze.pigSale.common.Result;
import com.ze.pigSale.constants.EsConstants;
import com.ze.pigSale.entity.Category;
import com.ze.pigSale.entity.EsProduct;
import com.ze.pigSale.entity.Product;
import com.ze.pigSale.repository.EsProductRepository;
import com.ze.pigSale.service.CategoryService;
import com.ze.pigSale.service.EsProductService;
import com.ze.pigSale.service.ProductService;
import jodd.util.StringUtil;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.completion.Completion;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.ze.pigSale.constants.EsConstants.*;

/**
 * @author zeb
 * @Date 2023-08-07 15:33
 */
@Service
public class EsProductServiceImpl implements EsProductService {

    @Autowired
    private EsProductRepository esProductRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Override
    public int importAll() {
        //从数据库查询所有商品
        List<Product> list = productService.list();
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }

        //处理种类
        List<EsProduct> esProductList = list.stream().map(this::getEsProduct).collect(Collectors.toList());

        //导入索引库
        Iterable<EsProduct> esProducts = esProductRepository.saveAll(esProductList);

        //计算记录数
        int count = 0;
        for (EsProduct esProduct : esProducts) {
            count++;
        }
        return count;
    }

    @Override
    public EsProduct getEsProduct(Product item) {
        if (item == null) {
            return null;
        }
        //处理种类名
        Long categoryId = item.getCategoryId();
        Category category = categoryService.getCategoryById(categoryId);
        if (category == null) {
            throw new CustomException("种类不存在");
        }
        String categoryName = category.getCategoryName();
        //属性拷贝
        EsProduct esProduct = new EsProduct();
        BeanUtils.copyProperties(item, esProduct);
        esProduct.setCategoryName(categoryName);

        // 自动补全字段的处理
        ArrayList<String> list = new ArrayList<>();

        // 添加种类、产地、商品名称
        list.add(esProduct.getCategoryName());
        list.add(esProduct.getProductName());
        list.add(esProduct.getOrigin());

        Completion completion = new Completion(list);
        esProduct.setSuggestion(completion);
        return esProduct;
    }

    @Override
    public void delete(Long id) {
        esProductRepository.deleteById(id);
    }

    @Override
    public EsProduct create(Long id) {
        Product product = productService.getProductById(id);
        EsProduct esProduct = this.getEsProduct(product);
        if (esProduct == null) {
            return null;
        }
        return esProductRepository.save(esProduct);
    }

    @Override
    public void delete(List<Long> ids) {
        esProductRepository.deleteAllById(ids);
    }

    @Override
    public Page<EsProduct> search(String keyword, Integer pageNum, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
        if (StringUtil.isBlank(keyword)) {
            return esProductRepository.findAll(pageRequest);
        }
        return esProductRepository.findByProductNameOrCategoryNameOrDescription(keyword, keyword, keyword, pageRequest);
    }

    @Override
    public Result<List> suggest(String prefix) {
        try {
            //创建索引库
            SearchRequest request = new SearchRequest(SEARCH_INDEX_REQUEST);
            //根据前缀补全
            request.source().suggest(new SuggestBuilder().
                    addSuggestion(
                            PRODUCT_SUGGEST,
                            SuggestBuilders
                                    .completionSuggestion(SUGGESTION_FIELD)
                                    .size(SUGGESTION_SIZE)
                                    .skipDuplicates(true)
                                    .prefix(prefix)
                    ));
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            //解析结果
            ArrayList<String> list = handleSuggestion(response);
            return Result.success(list);
        } catch (IOException e) {
            throw new CustomException("自动补全出现异常");
        }
    }

    private ArrayList<String> handleSuggestion(SearchResponse response) {
        Suggest suggest = response.getSuggest();
        CompletionSuggestion suggestion = suggest.getSuggestion(PRODUCT_SUGGEST);

        ArrayList<String> list = new ArrayList<>();
        for (CompletionSuggestion.Entry.Option option : suggestion.getOptions()) {
            String str = option.getText().toString();
            list.add(str);
        }
        return list;
    }

    @Override
    public Result<Map<String, List>> aggregate(String keyword, Integer pageNum, Integer pageSize) {
        try {
            //创建索引库
            SearchRequest request = new SearchRequest(EsConstants.SEARCH_INDEX_REQUEST);
            if (!StringUtil.isEmpty(keyword)) {
                //根据关键字搜索
                request.source()
                        .query(QueryBuilders.matchQuery(EsConstants.SEARCH_KEYWORDS, keyword));
            }
            //种类聚合和产地聚合
            request.source().size(0)
                    .aggregation(AggregationBuilders
                            .terms(EsConstants.CATEGORY_AGGREGATION).field(CATEGORY_FIELD).size(100))
                    .aggregation(AggregationBuilders
                            .terms(ORIGIN_AGGREGATION).field(ORIGIN_FIELD).size(100));
            //发送请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            //解析结果
            HashMap<String, List> map = handleAggregation(response);
            //返回
            return Result.success(map);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("聚合异常");
        }
    }

    private HashMap<String, List> handleAggregation(SearchResponse response) {
        Aggregations aggregations = response.getAggregations();
        List<String> categoryAgg = buildAggregation(aggregations, CATEGORY_AGGREGATION);
        List<String> originAgg = buildAggregation(aggregations, ORIGIN_AGGREGATION);
        HashMap<String, List> map = new HashMap<>(AGGREGATION_SIZE);
        map.put(CATEGORY_FIELD, categoryAgg);
        map.put(ORIGIN_FIELD, originAgg);
        return map;
    }

    private List<String> buildAggregation(Aggregations aggregations, String name) {
        if (aggregations == null) {
            return Collections.emptyList();
        }
        Terms agg = aggregations.get(name);
        if (agg == null) {
            return Collections.emptyList();
        }
        List<? extends Terms.Bucket> buckets = agg.getBuckets();
        List<String> list = new ArrayList<>();
        for (Terms.Bucket bucket : buckets) {
            String key = bucket.getKeyAsString();
            list.add(key);
        }
        return list;
    }

}

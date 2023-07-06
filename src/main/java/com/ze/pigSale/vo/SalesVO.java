package com.ze.pigSale.vo;

import lombok.Data;

import java.util.Collection;
import java.util.Set;

/**
 * @author zeb
 * @Date 2023-06-04 15:43
 */
@Data
public class SalesVO {
    private Set<String> productNames;
    private Collection<Integer> sales;
}

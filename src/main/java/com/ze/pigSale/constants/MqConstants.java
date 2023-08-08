package com.ze.pigSale.constants;

/**
 * @author zeb
 * @Date 2023-08-07 22:31
 */
public class MqConstants {
    public static final String INSERT_QUEUE_NAME = "product.insert.queue";
    public static final String DELETE_QUEUE_NAME = "product.delete.queue";
    public static final String EXCHANGE_NAME = "product.topic";
    public static final String DELETE_KEY = "product.delete";
    public static final String INSERT_KEY = "product.insert";
    public static final String ORDER_QUEUE_NAME = "order.queue";



}

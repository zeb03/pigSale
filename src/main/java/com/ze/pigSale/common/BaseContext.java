package com.ze.pigSale.common;

/**
 * author: zebii
 * Date: 2023-01-30-0:39
 */
/*
    基于ThreadLocal封装工具类，用于保存和获取用户id
 */
public class BaseContext {
    private static final ThreadLocal<Long> THREAD_LOCAL = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        THREAD_LOCAL.set(id);
    }

    public static Long getCurrentId() {
        return THREAD_LOCAL.get();
    }

    public static void removeThreadLocal(){
        THREAD_LOCAL.remove();
    }
}

package com.ze.pigSale.common;

public class RedisConstants {
    public static final String DEFAULT_USERNAME = "user_";
    public static final Integer USER_NOT_EXISTS = 3;
    public static final Integer USER_DISABLED = 2;
    public static final String LOGIN_CODE_KEY = "login:code:";
    public static final Long LOGIN_CODE_TTL = 2L;
    public static final String LOGIN_USER_KEY = "login:token:";
    public static final Long LOGIN_USER_TTL = 36000L;

    public static final Long CACHE_NULL_TTL = 2L;

    public static final Long CACHE_SHOP_TTL = 30L;
    public static final String CACHE_SHOP_KEY = "cache:shop:";

    public static final String LOCK_SHOP_KEY = "lock:shop:";

    public static final String COLLECT_USER_KEY = "collect:shop:";

    public static final String FEED_SHOP_KEY = "feed:shop:";
    public static final String FEED_ORDER_KEY = "feed:order:";
    public static final String FEED_FEEDBACK_KEY = "feed:feedback:";

    public static final String SECKILL_STOCK_KEY = "seckill:stock:";
    public static final String BLOG_LIKED_KEY = "blog:liked:";
    public static final String SHOP_GEO_KEY = "shop:geo:";
    public static final String USER_SIGN_KEY = "sign:";
}

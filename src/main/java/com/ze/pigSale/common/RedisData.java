package com.ze.pigSale.common;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RedisData {
    //逻辑过期时间
    private LocalDateTime expireTime;
    //数据
    private Object data;
}

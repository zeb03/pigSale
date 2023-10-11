/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ze.pigSale.common;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

@Data
public class Result<T> {

    private Integer code; // 编码：200成功，0和其它数字为失败

    private String msg; // 错误信息

    private T data; // 数据

    private Map map = new HashMap(); // 动态数据

    public static <T> Result<T> success(T object) {
        Result<T> r = new Result<T>();
        r.data = object;
        r.code = 200;
        return r;
    }

    public static <T> Result<T> error(String msg) {
        Result r = new Result();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public static <T> Result<T> error(Integer code, String msg) {
        Result r = new Result();
        r.msg = msg;
        r.code = code;
        return r;
    }

    public Result<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}

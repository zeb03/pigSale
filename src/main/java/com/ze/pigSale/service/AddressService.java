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

package com.ze.pigSale.service;

import com.github.pagehelper.PageInfo;
import com.ze.pigSale.entity.Address;

import java.util.List;

/**
 * author: zebii
 * Date: 2023-03-21-10:11
 */

public interface AddressService {

    /**
     * 查询所有的address
     * @param userId
     * @return
     */
    List<Address> getAddressList(Long userId);

    /**
     * 根据id获取地址
     * @param addressId
     * @return
     */
    Address getAddressById(Long addressId);

    /**
     * 插入address
     * @param address
     */
    void insertAddress(Address address);

    /**
     * 批量插入
     * @param addressList
     */
    void insertBatch(List<Address> addressList, Long userId);

    /**
     * 修改address
     * @param address
     */
    void updateAddress(Address address);

    /**
     * 删除address
     * @param addressId
     */
    void deleteAddress(Long addressId);
}

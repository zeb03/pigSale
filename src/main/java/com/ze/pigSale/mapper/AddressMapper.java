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

package com.ze.pigSale.mapper;

import com.ze.pigSale.entity.Address;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author z
* @description 针对表【address】的数据库操作Mapper
* @createDate 2023-03-13 15:32:23
* @Entity com.ze.pig.entity.Address
*/
@Mapper
public interface AddressMapper {

    /**
     * 查询所有的address
     * @return
     */
    List<Address> getAddressListByUserId(@Param("userId") Long userId);

    /**
     * 根据id获取地址
     * @param addressId
     * @return
     */
    Address getAddressById(@Param("addressId") Long addressId);

    /**
     * 插入address
     * @param address
     */
    void insertAddress(Address address);

    /**
     * 批量插入
     * @param addressList
     */
    void insertBatch(List<Address> addressList);

    /**
     * 修改address
     * @param address
     */
    void updateAddress(Address address);

    /**
     * 删除address
     * @param addressId
     */
    void deleteAddress(@Param("addressId") Long addressId);

    /**
     * 将其他默认地址取消
     * @param address
     */
    void updateIsDefault(Address address);
}

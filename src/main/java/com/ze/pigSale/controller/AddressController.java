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

package com.ze.pigSale.controller;

import com.ze.pigSale.common.BaseContext;
import com.ze.pigSale.common.Result;
import com.ze.pigSale.entity.Address;
import com.ze.pigSale.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * author: zebii
 * Date: 2023-03-21-10:08
 */
@RestController
@Slf4j
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping()
    public Result<List<Address>> list() {
        Long userId = BaseContext.getCurrentId();
        log.info("查看地址userId:{}", userId);
        List<Address> addressList = addressService.getAddressList(userId);
        return Result.success(addressList);
    }

    @PostMapping()
    public Result<String> add(@RequestBody Address address) {
        Long userId = BaseContext.getCurrentId();
        log.info("添加地址userId:{}", userId);
        address.setUserId(userId);
        addressService.insertAddress(address);
        // addressService.insertBatch(addressList,userId);
        return Result.success("添加成功");
    }

    @PutMapping()
    public Result<Address> edit(@RequestBody Address address) {
        Long userId = BaseContext.getCurrentId();
        log.info("编辑地址userId:{}", userId);
        log.info("编辑地址: {}", address);
        address.setUserId(userId);
        addressService.updateAddress(address);
        return Result.success(address);
    }

    @DeleteMapping("/{addressId}")
    public Result<String> remove(@PathVariable("addressId") Long addressId) {
        addressService.deleteAddress(addressId);
        return Result.success("删除成功");
    }
}

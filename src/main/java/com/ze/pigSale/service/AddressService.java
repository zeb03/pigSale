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

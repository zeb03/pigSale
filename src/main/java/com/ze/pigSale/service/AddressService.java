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
     * @return
     */
    List<Address> getAddressList();

    /**
     * 插入address
     * @param address
     */
    void insertAddress(Address address);

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

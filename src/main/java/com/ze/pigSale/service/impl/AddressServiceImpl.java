package com.ze.pigSale.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.ze.pigSale.entity.Address;
import com.ze.pigSale.mapper.AddressMapper;
import com.ze.pigSale.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author: zebii
 * Date: 2023-03-21-10:12
 */
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public List<Address> getAddressList() {
        return addressMapper.getAddressList();
    }

    @Override
    public void insertAddress(Address address) {
        addressMapper.insertAddress(address);
    }

    @Override
    public void updateAddress(Address address) {
        addressMapper.updateAddress(address);
    }

    @Override
    public void deleteAddress(Long addressId) {
        addressMapper.deleteAddress(addressId);
    }
}

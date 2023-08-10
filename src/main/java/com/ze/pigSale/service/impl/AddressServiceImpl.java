package com.ze.pigSale.service.impl;

import com.ze.pigSale.entity.Address;
import com.ze.pigSale.mapper.AddressMapper;
import com.ze.pigSale.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<Address> getAddressList(Long userId) {
        return addressMapper.getAddressListByUserId(userId);
    }

    @Override
    public Address getAddressById(Long addressId) {
        return addressMapper.getAddressById(addressId);
    }

    @Override
    public void insertAddress(Address address) {
        addressMapper.insertAddress(address);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertBatch(List<Address> addressList, Long userId) {
        for (Address address : addressList) {
            address.setUserId(userId);
        }
        addressMapper.insertBatch(addressList);
    }

    @Override
    public void updateAddress(Address address) {
        if (address.getIsDefault() == 1) {
            addressMapper.updateIsDefault(address);
        }
        addressMapper.updateAddress(address);

    }

    @Override
    public void deleteAddress(Long addressId) {
        addressMapper.deleteAddress(addressId);
    }
}

package com.ze.pigSale.controller;

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

    @GetMapping("/list")
    public Result<List<Address>> list(){
        List<Address> addressList = addressService.getAddressList();
        return Result.success(addressList);
    }

    @PostMapping
    public Result<Address> add(@RequestBody Address address){
        addressService.insertAddress(address);
        return Result.success(address);
    }

    @PutMapping
    public Result<Address> edit(@RequestBody Address address){
        addressService.updateAddress(address);
        return Result.success(address);
    }

    @DeleteMapping("/{addressId}")
    public Result<String> remove(@PathVariable("addressId") Long addressId){
        addressService.deleteAddress(addressId);
        return Result.success("删除成功");
    }
}

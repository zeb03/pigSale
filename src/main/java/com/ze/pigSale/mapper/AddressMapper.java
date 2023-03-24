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

}





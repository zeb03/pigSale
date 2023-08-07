package com.ze.pigSale.mapper;

import com.ze.pigSale.entity.UserProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author z
* @description 针对表【user_product】的数据库操作Mapper
* @createDate 2023-07-28 23:21:23
* @Entity com.ze.pigSale.entity.UserProduct
*/
@Mapper
public interface UserProductMapper extends BaseMapper<UserProduct> {

}





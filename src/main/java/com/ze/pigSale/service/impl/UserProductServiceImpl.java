package com.ze.pigSale.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ze.pigSale.entity.UserProduct;
import com.ze.pigSale.service.UserProductService;
import com.ze.pigSale.mapper.UserProductMapper;
import org.springframework.stereotype.Service;

/**
* @author z
* @description 针对表【user_product】的数据库操作Service实现
* @createDate 2023-07-28 23:21:23
*/
@Service
public class UserProductServiceImpl extends ServiceImpl<UserProductMapper, UserProduct>
    implements UserProductService{

}





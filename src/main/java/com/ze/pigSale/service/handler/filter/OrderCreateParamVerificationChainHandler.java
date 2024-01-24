package com.ze.pigSale.service.handler.filter;

import com.ze.pigSale.common.CustomException;
import com.ze.pigSale.dto.OrdersDTO;
import com.ze.pigSale.entity.Cart;
import com.ze.pigSale.entity.Product;
import com.ze.pigSale.service.CartService;
import com.ze.pigSale.service.ProductService;
import com.ze.pigSale.service.handler.OrderCreateChainHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 验证库存是否充足
 * @author zeb
 * @Date 2024-01-21 15:51
 */
@Slf4j
@Component
public class OrderCreateParamVerificationChainHandler implements OrderCreateChainHandler<OrdersDTO> {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private CartService cartService;
    @Resource
    private ProductService productService;

    private static final DefaultRedisScript<Long> ORDER_SCRIPT;

    static {
        ORDER_SCRIPT = new DefaultRedisScript<>();
        ORDER_SCRIPT.setLocation(new ClassPathResource("order.lua"));
        ORDER_SCRIPT.setResultType(Long.class);
    }

    @Override
    public void handle(OrdersDTO requestParam) {
        log.info("开始验证参数合法性");
        // 获取购物车商品
        List<Long> cartItemIds = requestParam.getCartItemIds();
        List<Cart> cartList = cartService.getCartListByIds(cartItemIds);
        if (cartList == null || cartList.isEmpty()) {
            throw new CustomException("商品不存在");
        }

        // 判断库存是否充足，最好使用Lua脚本(如何使用for)，保证查询和扣减一致性
        // 创建keys，保存商品id
        List<String> keys = new ArrayList<>(cartList.size());
        // 创建args，保存数量
        String[] args = new String[cartList.size() + 1];
        // 设置keys长度
        args[0] = String.valueOf(cartList.size());

        for (int i = 0; i < cartList.size(); i++) {
            Cart cart = cartList.get(i);

            Long productId = cart.getProductId();
            Integer quantity = cart.getQuantity();

            keys.add(productId.toString());
            args[i + 1] = quantity.toString();
        }

        // 执行lua脚本，返回商品id
        Long result = stringRedisTemplate.execute(ORDER_SCRIPT, keys, args);

        if (result == null || result != 0) {
            Product product = productService.getProductById(result);
            throw new CustomException("商品 [" + product.getProductName() + "] 库存不足，请重新选择");
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }

}

package com.ze.pigSale;

import com.ze.pigSale.entity.User;
import com.ze.pigSale.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class PigSaleApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
        User user = userService.getUserById(1);
        log.info("{}",user);
    }

}

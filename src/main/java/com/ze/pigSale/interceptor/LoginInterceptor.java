package com.ze.pigSale.interceptor;

import com.alibaba.fastjson.JSON;
import com.ze.pigSale.common.BaseContext;
import com.ze.pigSale.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * author: zebii
 * Date: 2023-01-28-20:35
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截到请求：{}", request.getRequestURI());

        Long user = (Long) request.getSession().getAttribute("user");
        if (user != null) {
            log.info("线程id: {}", Thread.currentThread().getId());
            BaseContext.setCurrentId(user);
            return true;
        }
        //暂时有问题
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(Result.error("not login")));
//        request.getRequestDispatcher("/page/login").forward(request, response);
//        response.sendRedirect("/page/login");
        return false;
    }
}

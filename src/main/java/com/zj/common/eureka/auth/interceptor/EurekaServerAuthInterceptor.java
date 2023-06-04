package com.zj.common.eureka.auth.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @Author: zhoujun
 * @Date: 2023/5/27 18:23
 */
@Slf4j
public class EurekaServerAuthInterceptor implements HandlerInterceptor {

    private final String authKey;

    private final String authToken;

    public EurekaServerAuthInterceptor(String authKey, String authToken) {
        this.authKey = authKey;
        this.authToken = authToken;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        final String reqAuthToken = request.getHeader(authKey);
        if (StringUtils.isBlank(reqAuthToken)) {
            log.info("######preHandle: authKey is blank 不允许调用操作！");
            return false;
        }


        log.info("######preHandle: auth right!");
        return true;
    }
}

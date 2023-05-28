package com.zj.common.eureka.auth;

import lombok.extern.slf4j.Slf4j;
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
        if (!(handler instanceof HandlerMethod)) {
            log.info("######preHandle: handler not HandlerMethod");
            return false;
        }
        final HandlerMethod handlerMethod = (HandlerMethod) handler;
        final Class<?> beanClass = handlerMethod.getBeanType();
        final EurekaAuthServer classAnnotation = beanClass.getAnnotation(EurekaAuthServer.class);
        // 类上的认证
        if (Objects.nonNull(classAnnotation)) {
            // 进行身份认证的逻辑
            // 如果认证通过，则返回 true，继续执行方法调用；否则返回 false，不执行方法调用
            log.info("类上标识了，走类上的认证逻辑。");
            request.setAttribute(authKey, authToken);
            return false;
        }
        // 如果方法标注了 @EurekaAuthServer 注解，则进行身份认证
        final EurekaAuthServer methodAnnotation = handlerMethod.getMethodAnnotation(EurekaAuthServer.class);
        // 方法上的认证
        if ( Objects.nonNull(methodAnnotation)) {
            log.info("类上没有，走方法上的逻辑。");
        }
        // 没有标注 @NeedAuthentication 注解的方法不需要认证
        return false;
    }
}

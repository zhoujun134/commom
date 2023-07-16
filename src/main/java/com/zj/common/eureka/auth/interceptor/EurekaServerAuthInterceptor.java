package com.zj.common.eureka.auth.interceptor;

import com.zj.common.encryption.EncryptionUtil;
import com.zj.common.exception.ResultCode;
import com.zj.common.exception.ValidateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 服务端处理授权信息
 * @Author: zhoujun
 * @Date: 2023/5/27 18:23
 */
@Slf4j
public class EurekaServerAuthInterceptor implements HandlerInterceptor {


    @Value("${eureka.server.auth.key:NONE}")
    private String authKey;

    @Value("${eureka.server.auth.token:NONE}")
    private String authToken;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        final String reqAuthToken = request.getHeader(authKey);
        ValidateUtil.exceptionByTrue(StringUtils.isBlank(reqAuthToken), ResultCode.NO_AUTH);
        final String sha256 = EncryptionUtil.hashWithSHA256(authToken);
        ValidateUtil.exceptionByFalse(StringUtils.equals(sha256, reqAuthToken), ResultCode.NO_AUTH);
        log.info("######preHandle: auth right!");
        return true;
    }
}

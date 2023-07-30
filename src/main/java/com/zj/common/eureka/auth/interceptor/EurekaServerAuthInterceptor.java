package com.zj.common.eureka.auth.interceptor;

import com.zj.common.encryption.EncryptionUtil;
import com.zj.common.exception.ResultCode;
import com.zj.common.exception.ValidateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    @Resource
    private FeignCheckUtil feignCheckUtil;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        final Map<String, String> headerMap = getRequestHeaderMap(request);
        final boolean isFeignRequest = feignCheckUtil.checkRequest(request.getRequestURI(), headerMap);
        if (Boolean.FALSE.equals(isFeignRequest)) {
            log.info("非 feign 的调用请求，不进行拦截！");
            return true;
        }
        final String reqAuthToken = request.getHeader(authKey);
        ValidateUtil.exceptionByTrue(StringUtils.isBlank(reqAuthToken), ResultCode.NO_AUTH);
        final String sha256 = EncryptionUtil.hashWithSHA256(authToken);
        ValidateUtil.exceptionByFalse(StringUtils.equals(sha256, reqAuthToken), ResultCode.NO_AUTH);
        log.info("######preHandle: auth right!");
        return true;
    }

    private static Map<String, String> getRequestHeaderMap(HttpServletRequest request) {
        final Enumeration<String> headerEnums = request.getHeaderNames();
        if (Objects.isNull(headerEnums)) {
            log.warn("异常请求 header 信息为空，request={}", request);
        }
        final Map<String, String> headerMap = new HashMap<>();
        while (headerEnums.hasMoreElements()) {
            final String oneHeaderName = headerEnums.nextElement();
            if (StringUtils.isBlank(oneHeaderName)) {
                continue;
            }
            final String oneHeaderValue = request.getHeader(oneHeaderName);
            headerMap.put(oneHeaderName, oneHeaderValue);
        }
        return headerMap;
    }
}

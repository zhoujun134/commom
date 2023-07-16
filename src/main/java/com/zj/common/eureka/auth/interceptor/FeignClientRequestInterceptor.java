package com.zj.common.eureka.auth.interceptor;

import com.zj.common.encryption.EncryptionUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collection;
import java.util.Map;

/**
 * @Author: zhoujun
 * @Date: 2023/5/27 19:52
 */
@Slf4j
public class FeignClientRequestInterceptor implements RequestInterceptor {


    @Value("${eureka.server.auth.key:NONE}")
    private String authKey;

    @Value("${eureka.server.auth.token:NONE}")
    private String authToken;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        final Map<String, Collection<String>> headers =
                requestTemplate.headers();
        log.info("FeignRequestInterceptor######apply: authKey={}, authToken={}", authKey, authToken);
        if (headers.containsKey(authKey)) {
            return;
        }
        requestTemplate.header(authKey, EncryptionUtil.hashWithSHA256(authToken));
    }
}

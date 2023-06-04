package com.zj.common.eureka.config;

import com.zj.common.constants.ConfigConstants;
import com.zj.common.eureka.auth.interceptor.EurekaServerAuthInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Objects;

/**
 * @Author: zhoujun
 * @Date: 2023/5/27 18:16
 */
@Configuration
public class EurekaFeignAuthConfiguration implements WebMvcConfigurer {

    @Value("eureka.server.auth.key:NONE")
    private String authKey;

    @Value("eureka.server.auth.token:NONE")
    private String authToken;

    @Value("eureka.server.auth.path.pattern:NONE")
    private String[] authPathPatterns;

    @Bean
    public EurekaServerAuthInterceptor eurekaServerAuthInterceptor() {
        if (StringUtils.equals(ConfigConstants.NONE.getCode(), authKey)) {
            return null;
        }
        return new EurekaServerAuthInterceptor(authKey, authToken);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        final EurekaServerAuthInterceptor eurekaServerAuthInterceptor = eurekaServerAuthInterceptor();
        if (Objects.isNull(eurekaServerAuthInterceptor)) {
            return;
        }
        if (Objects.isNull(authPathPatterns)
                || authPathPatterns.length == 0
                || StringUtils.equals(authPathPatterns[0], ConfigConstants.NONE.getCode())) {
            return;
        }
        registry.addInterceptor(eurekaServerAuthInterceptor())
                .addPathPatterns(authPathPatterns);
    }
}

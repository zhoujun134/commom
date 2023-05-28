package com.zj.common.eureka.config;

import com.zj.common.eureka.auth.EurekaServerAuthInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: zhoujun
 * @Date: 2023/5/27 18:16
 */
@Configuration
public class EurekaAuthConfiguration implements WebMvcConfigurer {

    @Value("eureka.server.auth.key")
    private String authKey;

    @Value("eureka.server.auth.token")
    private String authToken;
    @Bean
    public EurekaServerAuthInterceptor eurekaServerAuthInterceptor() {
        return new EurekaServerAuthInterceptor(authKey, authToken);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(eurekaServerAuthInterceptor())
                .addPathPatterns("/**");
    }
}

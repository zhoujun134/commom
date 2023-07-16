package com.zj.common.eureka.config;

import com.zj.common.constants.ConfigConstants;
import com.zj.common.eureka.auth.interceptor.EurekaServerAuthInterceptor;
import com.zj.common.eureka.auth.interceptor.FeignClientRequestInterceptor;
import com.zj.common.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * @Author: zhoujun
 * @Date: 2023/5/27 18:16
 */
@Configuration
@Slf4j
public class EurekaFeignAuthConfiguration implements WebMvcConfigurer {

    @Value("${eureka.server.auth.key:NONE}")
    private String authKey;

    @Value("${eureka.server.auth.token:NONE}")
    private String authToken;

    @Value("${eureka.server.auth.path.pattern:NONE}")
    private String[] authPathPatterns;

    @PostConstruct
    public void init(){
        log.info("EurekaFeignAuthConfiguration######inti: authKey={}, authToken={}, authPathPatterns={}",
                authKey, authToken, JsonUtil.toJSONString(authPathPatterns));
    }

    @Bean
    public EurekaServerAuthInterceptor eurekaServerAuthInterceptor() {
        if (StringUtils.equals(ConfigConstants.NONE.getCode(), authKey)) {
            log.info("######eurekaServerAuthInterceptor: authKey is null!");
            return null;
        }
        return new EurekaServerAuthInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        final EurekaServerAuthInterceptor eurekaServerAuthInterceptor = eurekaServerAuthInterceptor();
        if (Objects.isNull(eurekaServerAuthInterceptor)) {
            log.info("######addInterceptors: eurekaServerAuthInterceptor is null!");
            return;
        }
        if (Objects.isNull(authPathPatterns)
                || authPathPatterns.length == 0
                || StringUtils.equals(authPathPatterns[0], ConfigConstants.NONE.getCode())) {
            log.info("######addInterceptors: 需要认证的路径配置为空！authPathPatterns is empty ！");
            return;
        }
        registry.addInterceptor(eurekaServerAuthInterceptor())
                .addPathPatterns(authPathPatterns);
        log.info("######addInterceptors: registry.addInterceptor add success !");
    }

    @Bean
    public FeignClientRequestInterceptor feignRequestInterceptor() {
        return new FeignClientRequestInterceptor();
    }
}

package com.zj.common.eureka.auth.interceptor;

import com.zj.common.constants.ConfigConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * @Author: zhoujun
 * @Date: 2023/7/30 17:21
 */
@Slf4j
@Component
public class FeignCheckUtil {

    @Value("${feign.call.unique.identification:NONE}")
    private String callUniqueIdentification;

    public boolean checkRequest(String requestUrl, Map<String, String> headers) {
        if (StringUtils.isBlank(requestUrl) || Objects.isNull(headers) || headers.isEmpty()) {
            log.info("FeignCheckUtil######checkRequest:  requestUrl={} is blank, or headers ={} is empty !",
                    requestUrl, headers);
            return false;
        }
        if (!StringUtils.equals(callUniqueIdentification, ConfigConstants.NONE.getCode())
                && headers.containsKey(ConfigConstants.FEIGN_CALL_UNIQUE_IDENTIFICATION_KEY.getCode())) {
            log.info("FeignCheckUtil######checkRequest:  来自于 feign 的调用！其中的唯一标识为:" +
                    " FEIGN-CALL-UNIQUE-IDENTIFICATION-KEY={}", callUniqueIdentification);
            return true;
        }

        // Check the URL
        if (requestUrl.startsWith("http://eureka-server/eureka/v2/instances/") ||
                requestUrl.startsWith("https://eureka-server/eureka/v2/instances/") ) {
            log.info("FeignCheckUtil######checkRequest:  来自于 feign 的调用！url 开始为 eureka 的初始 url， 其为:" +
                    " requestUrl = {} ", requestUrl);
            return true;
        }

        // Check the headers
        if (headers.containsKey("X-Application-Context") && headers.containsKey("X-Real-IP")
                && headers.containsKey("X-Forwarded-For") && headers.containsKey("Accept-Encoding")) {
            log.info("FeignCheckUtil######checkRequest:  requestUrl={}, X-Application-Context={}," +
                            " X-Real-IP ={}, X-Forwarded-For={}, Accept-Encoding={}", requestUrl,
                    headers.get("X-Application-Context"), headers.get("X-Real-IP"),
                    headers.get("X-Forwarded-For"), headers.get("Accept-Encoding"));
            return true;
        }

        return false;
    }
}

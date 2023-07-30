package com.zj.common.constants;

import lombok.Getter;

/**
 * @Author: zhoujun
 * @Date: 2023/6/4 21:24
 */
@Getter
public enum ConfigConstants {
    NONE("NONE", "空值"),

    FEIGN_CALL_UNIQUE_IDENTIFICATION_KEY("FEIGN-CALL-UNIQUE-IDENTIFICATION-KEY", "Feign 调用的唯一标识"),

    ;
    private final String code;

    private final String value;

    ConfigConstants(String code, String value) {
        this.code = code;
        this.value = value;
    }
}

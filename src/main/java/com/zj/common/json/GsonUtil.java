package com.zj.common.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zj.common.exception.BusinessException;
import com.zj.common.exception.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zhoujun <junzhou134@gmail.com>
 * Created on 2020-11-23
 */
@Slf4j
public class GsonUtil {

    private static final Gson GSON = new Gson();

    public static String toJSONString(Object object) {
        try {
            return object == null ? null : GSON.toJson(object);
        } catch (Exception e) {
            log.error("GsonUtil######toJSONString 对象转换json异常! object={}", object, e);
            throw new BusinessException(ResultCode.OBJECT_TO_JSON_ERROR);
        }
    }

    public static <T> T parseObject(String text, Class<T> clazz) {
        T result = null;
        try {
            result = text == null ? null : GSON.fromJson(text, clazz);
        } catch (Exception e) {
            log.error("GsonUtil######parseObject 对象转换 json 异常! text={}, clazz={}", text, clazz, e);
        }
        return result;
    }

    public static <T> List<T> parseArray(String text, Class<T> clazz) {
        List<T> result = Collections.emptyList();
        try {
            if (StringUtils.isBlank(text)) {
                text = "[]";
            }
            result = GSON.fromJson(text, TypeToken.getParameterized(ArrayList.class, new Type[]{clazz}).getType());
        } catch (Exception e) {
            log.error("GsonUtil######parseArray 对象转换 array 异常! text={}, clazz={}", text, clazz, e);
        }
        return result;
    }

    public static <K, V> Map<K, V> parseMap(String text) {
        Map<K, V> result = Collections.emptyMap();
        try {
            if (StringUtils.isBlank(text)) {
                text = "{}";
            }
            result = GSON.fromJson(text, new TypeToken<Map<K, V>>() {
            }.getType());
        } catch (Exception e) {
            log.error("GsonUtil######parseMap 对象转换 map 异常! text={}, clazz={}", text, e);
        }
        return result;
    }
}
package com.zj.common.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zj.common.exception.BusinessException;
import com.zj.common.exception.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.*;

/**
 * @author zhoujun <junzhou134@gmail.com>
 * Created on 2020-11-23
 */
@Slf4j
public class JsonUtil {

    private static final Gson GSON = new Gson();

    private static final Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * 将 object 转换为 json
     * @param object 待转换的 json
     * @return 转换之后的 json 串
     */
    public static String toJSONString(Object object) {
        try {
            return Objects.isNull(object) ? null : GSON.toJson(object);
        } catch (Exception e) {
            log.error("GsonUtil######toJSONString 对象转换json异常! object={}", object, e);
            throw new BusinessException(ResultCode.OBJECT_TO_JSON_ERROR);
        }
    }

    /**
     * 转义的 json 串
     * @param object 待转换的对象
     * @return 转义之后的对象
     */
    public static String toPrettyJsonString(Object object) {
        try {
            return Objects.isNull(object) ? null : PRETTY_GSON.toJson(object);
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
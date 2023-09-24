package com.zj.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

/**
 * @author junzhou
 * @date 2022/9/18 10:43
 * @since 1.8
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ResultCode {

    /**
     * 通用类 异常
     */
    SUCCESS(0, "ok"),

    FAIL(-1, "请求错误，请联系管理员！"),

    /**
     * 当前用户未登录!
     */
    USER_NOT_LOGIN(1, "当前用户未登录! "),
    /**
     * 账号类型异常
     */
    ACCOUNT_TYPE_ERROR(2, "账号类型错误! "),
    /**
     * 登录错误
     */
    LOGIN_ERROR(3, "登录错误！"),
    /**
     * 参数错误
     */
    PARAMETER_ERROR(4, "参数错误"),

    /**
     * 对象转换 json 异常
     */
    OBJECT_TO_JSON_ERROR(5, "对象转换 json 异常! "),

    /**
     * cookie 中不存在
     */
    COOKIE_NOT_CONTAIN_TOKEN(6, "请求的 cookie 中不包含登录所需要的信息！"),

    /**
     * 生成普通用户角色关系失败
     */
    DB_CREATE_COMMON_USER_ROLE_FAILED(7, "生成普通用户角色关系失败!"),

    /**
     * 用户信息入库失败！
     */
    DB_USER_INFO_INSERT_FAILED(8, "用户信息入库失败！"),
    DB_USER_NOT_LOGIN_NO_PERMIT(9, "用户没有登录，没有数据库的操作权限！"),
    QUERY_MENU_LIST_USER_NOT_LOGIN_NO_PERMIT(10, "用户没有登录，无法获取菜单列表！"),
    ENCRYPTION_UTIL_ASE_NOT_SUPPORT_EXCEPTION(11, "EncryptionUtil######generateAESKey keyGenerator is null, Not Supported !"),
    ENCRYPTION_UTIL_SHA256_NOT_SUPPORT_EXCEPTION(12, "EncryptionUtil Sha256 Not Supported !"),
    ENCRYPTION_UTIL_RSA_NOT_SUPPORT_EXCEPTION(13, "EncryptionUtil RSA Not Supported !"),
    ENCRYPTION_UTIL_RSA_KEY_PAIR_NOT_SUPPORT_EXCEPTION(13, "EncryptionUtil RSA keyPairGenerator Not Supported !"),
    /**
     * 无权限操作
     */
    NO_AUTH(401, "未授权，无权访问！"),
    /**
     * 无权限操作
     */
    NO_OPERATOR_AUTH(403, "无权限操作"),
    /**
     * 系统错误
     */
    SYSTEM_ERROR(500, "出现错误"),

    /**
     * code 从 1000 开始，表示 请求相关的异常信息
     * 未登录
     */
    ANONYMOUS_USER_EXCEPTION(1000, "用户未登录，请登录后尝试！"),

    PHONE_INFO_EXISTED(1001, "手机号已经存在！"),
    USERNAME_INFO_EXISTED(1002, "用户名已经存在！"),
    EMAIL_INFO_EXISTED(1003, "邮件已经存在！"),


    /**
     * 文件类 异常， code 从 2000 开始
     */
    FILE_NOT_FOUND(2000, "%s 文件不存在！"),


    ;

    /**
     * 异常 code
     */
    private int code;

    /**
     * 异常信息
     */
    private String message;

    /**
     * 根据 code 获取 resultCode 异常信息
     *
     * @param code 报错 code
     * @return 对应 code 的异常结果
     */
    public static ResultCode getByCode(int code) {
        return Stream.of(ResultCode.values())
                .filter(resultCode -> resultCode.getCode() == code)
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取带参数的 resultCode 信息
     *
     * @param resultCode 原始的 resultCode
     * @param args       参数信息
     * @return 更新参数后的 resultCode
     */
    public static ResultCode getResultCode(ResultCode resultCode, Object... args) {
        String message = resultCode.getMessage();
        resultCode.message = String.format(message, args);
        return resultCode;
    }

    /**
     * 获取带参数的 resultCode 信息
     *
     * @param resultCode 原始的 resultCode
     * @param args       参数信息
     * @return 更新参数后的 resultCode
     */
    public static ResultCode getResultCode(ResultCode resultCode, Object args) {
        String message = resultCode.getMessage();
        resultCode.message = String.format(message, args);
        return resultCode;
    }
}

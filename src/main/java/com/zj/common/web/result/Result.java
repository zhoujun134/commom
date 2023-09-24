package com.zj.common.web.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zj.common.exception.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author junzhou
 * @date 2022/9/18 18:15
 * @since 1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {

    private int code;

    private String message;

    private T result;

    public Result(ResultCode resultCode, T result) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.result = result;
    }

    public Result(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }


    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(ResultCode resultCode, String message) {
        this.code = resultCode.getCode();
        this.message = message;
    }

    public static <T> Result<T> ok() {
        return new Result<>(ResultCode.SUCCESS);
    }

    public static <T> Result<T> ok(T result) {
        return new Result<>(ResultCode.SUCCESS, result);
    }

    public static <T> Result<T> fail() {
        return new Result<>(ResultCode.FAIL);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(ResultCode.FAIL.getCode(), message, null);
    }

    public static <T> Result<T> fail(T result) {
        return new Result<>(ResultCode.FAIL, result);
    }

    public static <T> Result<T> fail(int code, String message, T result) {
        return new Result<>(code, message, result);
    }


    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message);
    }

    public static <T> Result<T> fail(ResultCode resultCode, String message) {
        return new Result<>(resultCode, message);
    }
    public static <T> Result<T> fail(ResultCode resultCode) {
        return new Result<>(resultCode);
    }
}

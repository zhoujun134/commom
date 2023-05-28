package com.zj.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author junzhou
 * @date 2022/9/18 18:14
 * @since 1.8
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(AnonymousException.class)
    public Result<String> handleAnonymousException(AnonymousException exception, HttpServletResponse response) {
        // 匿名用户异常
        response.setStatus(401);
        log.error("ExceptionHandle  --> anonymousException error: ", exception);
        return Result.fail(ResultCode.ANONYMOUS_USER_EXCEPTION.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public Result<String> handleBusinessException(BusinessException businessException) {
        log.error("ExceptionHandle  --> businessException error: ", businessException);
        return Result.fail(businessException.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception exception) {
        log.error("ExceptionHandle  --> exception error: ", exception);
        if (StringUtils.isBlank(exception.getMessage())) {
            return Result.fail(ResultCode.FAIL);
        }
        return Result.fail(exception.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    public Result<String> handleThrowable(Throwable exception) {
        log.error("ExceptionHandle  --> throwable error: ", exception);
        return Result.fail(ResultCode.FAIL.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("ExceptionHandle######methodArgumentNotValidException !", exception);
        BindingResult bindingResult = exception.getBindingResult();
        StringBuilder result = new StringBuilder();
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        if (!CollectionUtils.isEmpty(allErrors)) {
            List<String> errorList = allErrors.stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            String errorMessage = String.join(",", errorList);
            result.append(errorMessage);
        }
        String errorMsg = result.toString();
        if (StringUtils.isNotBlank(errorMsg)) {
            return Result.fail(errorMsg);
        }
        return Result.fail(ResultCode.FAIL.getMessage());
    }

}

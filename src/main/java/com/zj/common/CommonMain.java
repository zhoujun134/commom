package com.zj.common;

import org.springframework.aop.framework.AopContext;

/**
 * @Author: zhoujun
 * @Date: 2023/5/27 17:45
 */
public class CommonMain {
    public static void main(String[] args) {
        Object o = AopContext.currentProxy();
    }
}

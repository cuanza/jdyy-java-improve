package com.jdyy.commons.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author:陈镇川
 * @QQ:1026551395
 * @Date:2024/11/23 16:19
 **/


/**
 * 错误枚举
 */
@Getter
@AllArgsConstructor
public enum ErrorEnum {

    F0001("请选择要关注的人"), // 一级宏观错误码
    U0001("用户未登录");

    // ------------------------------------------------------------------------------- //



    private final String message;

}


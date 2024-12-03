package com.jdyy.commons.util;

import cn.dev33.satoken.stp.StpUtil;
import com.jdyy.commons.exception.BusinessException;
import lombok.SneakyThrows;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * @Author:陈镇川
 * @QQ:1026551395
 * @Date:2024/11/23 16:05
 **/


public class AssertUtil {
    //如果不是true，则抛异常
    @SneakyThrows
    public static void isTrue(boolean expression, ErrorEnum msg) {
        if (!expression) {
            throw new BusinessException(msg);
        }
    }

    //如果不是false，则抛异常
    @SneakyThrows
    public static void isFalse(boolean expression, ErrorEnum msg) {
        if (expression) {
            throw new BusinessException(msg);
        }
    }

    //如果是空或者空字符串，则抛异常
    @SneakyThrows
    public static void isNotEmpty(String str, ErrorEnum msg) {
        if (!StringUtils.hasText(str)) {
            throw new BusinessException(msg);
        }
    }

    //如果数组为空或者长度小于1，则抛异常
    @SneakyThrows
    public static void isNotEmpty(Object[] str, ErrorEnum msg) {
        if (ObjectUtils.isEmpty(str)) {
            throw new BusinessException(msg);
        }
    }

    //如果集合为空或者长度小于1，则抛异常
    @SneakyThrows
    public static void isNotEmpty(Collection collection, ErrorEnum msg) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new BusinessException(msg);
        }
    }

    //如果集合为空或者长度小于1，则抛异常
    @SneakyThrows
    public static void isNotEmpty(Map map, ErrorEnum msg) {
        if (CollectionUtils.isEmpty(map)) {
            throw new BusinessException(msg);
        }
    }

    //如果对象空，则抛异常
    @SneakyThrows
    public static void isNotNull(Object object, ErrorEnum msg) {
        if (object == null) {
            throw new BusinessException(msg);
        }
    }

    //如果对象空，则抛异常
    @SneakyThrows
    public static void isNotNull(Object[] objects, ErrorEnum msg) {
        if (ObjectUtils.isEmpty(objects)) {
            throw new BusinessException(msg);
        }
    }

    //比较对象
    @SneakyThrows
    public static void equals(Object obj1, Object obj2, ErrorEnum msg) {
        if (!equals(obj1, obj2)) {
            throw new BusinessException(msg);
        }
    }

    //比较对象的相等
    public static boolean equals(final Object cs1, final Object cs2) {
        if (cs1 == cs2) {
            return true;
        }
        if (null == cs1 && null != cs2) {
            return false;
        }
        if (null != cs1 && null == cs2) {
            return false;
        }
        if (cs1 == null || null == cs2) {
            return true;
        }
        if (cs1 instanceof String && cs2 instanceof String) {
            return cs1.equals(cs2);
        }
        return cs1.equals(cs2);
    }

    public static void mustLogin(String accessToken) {
        if (!isValidAccessToken(accessToken)) {
            throw new BusinessException(ErrorEnum.U0001);
        }
    }
    //判断当前token是否一致
    private static boolean isValidAccessToken(String accessToken) {
        return StpUtil.getTokenName().equals(accessToken);
    }
}

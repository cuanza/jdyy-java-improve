package com.jdyy.service.impl;

/**
 * @Author:陈镇川
 * @QQ:1026551395
 * @Date:2024/11/23 20:12
 **/

import cn.dev33.satoken.stp.StpInterface;
import com.jdyy.service.UserService;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限加载接口实现类
 */
@Component    // 保证此类被 SpringBoot 扫描，完成 Sa-Token 的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {


    @Resource
    private UserService userService;
    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        //实际项目中要根据具体业务逻辑来查询权限
        List<String> lists =userService.getPermissionsById(loginId);
        return lists;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        //实际项目中要根据具体业务逻辑来查询角色
        List<String>lists=userService.getRolesById(loginId);

        return lists;
    }

}

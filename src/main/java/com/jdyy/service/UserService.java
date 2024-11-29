package com.jdyy.service;

import com.jdyy.commons.util.Result;
import com.jdyy.entity.User;
import com.jdyy.entity.vo.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author LvXinming
 * @since 2022/10/13
 */


public interface UserService {

    //分页查询
    Result getUserPage(Page<User> page);

    //查所有
    Result findAll();

    //添加用户
    Result addUser(User user);

    //修改用户图片地址
    Result modifyUser(User user);

    //删除用户
    Result removeUser(User user);

    //登录
    Result login(User userLogin);

    //退出
    Result logout(String tokenValue);

    //注册
    Result register(User userLogin, MultipartFile avatarFile);

    //根据Id获取所有用户
    Result getUserById(Integer uid);

    //获取当前用户的权限标识
    List<String> getPermissionsById(Object uid);

    //获取当前用户的角色标识
    List<String> getRolesById(Object uid);
}
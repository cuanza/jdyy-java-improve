package com.jdyy.mapper;

import com.jdyy.entity.User;
import com.jdyy.entity.vo.Page;
import com.jdyy.entity.vo.SignInUserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author LvXinming
 * @since 2022/10/13
 */

@Mapper
public interface UserMapper {

    //分页查询
    List<User> getUserPage(Page<User> page);

    //查用户总数
    int countUser();

    //查询所有
    List<User> getAll();


    //根据Id获取所有用户
    @Select("select * from user where uid = #{uid};")
    SignInUserInfo getUserById(Integer uid);

    //查一个用户
    User getOneUser(User user);

    //添加用户
    int addUser(User user);

    //修改用户
    int modifyUser(User user);

    //删除用户
    int removeUser(User user);

    //登录
    User login(User user);

    //获取当前用户的角色标识
    List<String> getRolesById(Object uid);

    //获取当前用户的权限标识
    List<String> getPermissionsById(Object uid);



    //根据注册的id插入角色
    @Insert("insert into user_role(user_id,role_id) values(#{uid},#{rid});")
    int insertRolesById(Integer uid,Integer rid);

    //根据注册的id插入权限
    @Insert("insert into user_permission(user_id,permission_id) values(#{uid},#{pid});")
    int insertPermissionsById(Integer uid, Integer pid);



    /**
     * 为了解决添加失败后自增不连续的问题
     */
    @Update("alter table user AUTO_INCREMENT=1;")
    void fixAutoincrement();
}
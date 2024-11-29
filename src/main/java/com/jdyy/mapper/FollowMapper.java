package com.jdyy.mapper;

import com.jdyy.entity.Follow;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FollowMapper {
    /**
     * 查询关注信息
     * @param userId
     * @param followUserId
     * @return
     */
    @Select("select id,user_id,follow_user_id,is_valid from user_follow " +
            "where user_id=#{userId} and follow_user_id = #{followUserId}")
    Follow selectFollow(@Param("userId") Integer userId,@Param("followUserId") Integer followUserId);

    /**
     * 添加关注信息
     * @param userId
     * @param followUserId
     * @return
     */
    @Insert("insert into user_follow(user_id,follow_user_id,is_vaild,create_date,update_date) " +
            "values(#{userId},#{followUserId},1,now(),now())")
    int save(@Param("userId") Integer userId,@Param("followUserId") Integer followUserId);

    /**
     * 修改关注信息
     * @param id
     * @param isFollowed
     * @return
     */
    @Update("update user_follow set is_valid = #{isFollowed},update_date=now() where id=#{id}")
    int update(@Param("id") Integer id,@Param("isFollowed") int isFollowed);


















}

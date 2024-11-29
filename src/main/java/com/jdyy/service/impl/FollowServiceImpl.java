//package com.jdyy.service.impl;
//
//
//import cn.hutool.core.bean.BeanUtil;
//import com.jdyy.commons.util.AssertUtil;
//import com.jdyy.commons.util.Result;
//import com.jdyy.commons.util.Status;
//import com.jdyy.mapper.UserMapper;
//import org.springframework.stereotype.Service;
//
//import com.jdyy.mapper.FollowMapper;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import javax.annotation.Resource;
//import java.util.LinkedHashMap;
//
//import static com.jdyy.commons.util.ErrorEnum.F0001;
//
///**
// * 关注/取关业务逻辑实现层
// * @Author:陈镇川
// * @QQ:1026551395
// * @Date:2024/11/23 15:51
// **/
//
//
//@Service
//public class FollowServiceImpl {
//
////    @Value("${service.name.ms-oauth-server}")
////    private String oauthServerName;
////    @Value("${service.name.ms-diners-server}")
////    private String dinersServerName;
//    @Resource
//    private RestTemplate restTemplate;
//    @Resource
//    private FollowMapper followMapper;
//    @Resource
//    private UserMapper userMapper;
//    @Resource
//    private RedisTemplate redisTemplate;
//
//
//    /**
//     * 关注/取关
//     *
//     * @param followUserId 关注的食客ID
//     * @param isFollowed    是否关注 1=关注 0=取关
//     * @param accessToken   登录用户token
//     * @return
//     */
//    public Result follow(Integer followUserId, int isFollowed,
//                         String accessToken) {
//        // 是否选择了关注对象
//        AssertUtil.isTrue(followUserId == null || followUserId < 1,F0001);
//        // 获取登录用户信息 (封装方法)
//        SignInUserInfo dinerInfo = loadSignInDinerInfo(accessToken);
//        // 获取当前登录用户与需要关注用户的关注信息
//        Follow follow = followMapper.selectFollow(dinerInfo.getId(), followUserId);
//
//        // 如果没有关注信息，且要进行关注操作 -- 添加关注
//        if (follow == null && isFollowed == 1) {
//            // 添加关注信息
//            int count = followMapper.save(dinerInfo.getId(), followUserId);
//            // 添加关注列表到 Redis
//            if (count == 1) {
//                addToRedisSet(dinerInfo.getId(), followUserId);
//            }
//            return Result.success(200,"关注成功",  "关注成功");
//        }
//
//        // 如果有关注信息，且目前处于关注状态，且要进行取关操作 -- 取关关注
//        if (follow != null && follow.getIsValid() == 1 && isFollowed == 0) {
//            // 取关
//            int count = followMapper.update(follow.getId(), isFollowed);
//            // 移除 Redis 关注列表
//            if (count == 1) {
//                removeFromRedisSet(dinerInfo.getId(), followUserId);
//            }
//            return ResultInfoUtil.build(ApiConstant.SUCCESS_CODE,
//                    "成功取关", path, "成功取关");
//        }
//
//        // 如果有关注信息，且目前处于取关状态，且要进行关注操作 -- 重新关注
//        if (follow != null && follow.getIsValid() == 0 && isFollowed == 1) {
//            // 重新关注
//            int count = followMapper.update(follow.getId(), isFollowed);
//            // 添加关注列表到 Redis
//            if (count == 1) {
//                addToRedisSet(dinerInfo.getId(), followUserId);
//            }
//            return ResultInfoUtil.build(ApiConstant.SUCCESS_CODE,
//                    "关注成功", path, "关注成功");
//        }
//
//        return ResultInfoUtil.buildSuccess(path, "操作成功");
//    }
//
//    /**
//     * 添加关注列表到 Redis
//     *
//     * @param dinerId
//     * @param followUserId
//     */
//    private void addToRedisSet(Integer dinerId, Integer followUserId) {
//        redisTemplate.opsForSet().add(RedisKeyConstant.following.getKey() + dinerId, followUserId);
//        redisTemplate.opsForSet().add(RedisKeyConstant.followers.getKey() + followUserId, dinerId);
//    }
//
//    /**
//     * 移除 Redis 关注列表
//     *
//     * @param dinerId
//     * @param followUserId
//     */
//    private void removeFromRedisSet(Integer dinerId, Integer followUserId) {
//        redisTemplate.opsForSet().remove(RedisKeyConstant.following.getKey() + dinerId, followUserId);
//        redisTemplate.opsForSet().remove(RedisKeyConstant.followers.getKey() + followUserId, dinerId);
//    }
//
//    /**
//     * 获取登录用户信息
//     *
//     * @param accessToken
//     * @return
//     */
//    private SignInUserInfo loadSignInDinerInfo(String accessToken) {
//        // 必须登录
//        AssertUtil.mustLogin(accessToken);
//
//        Result resultInfo = userMapper.getUserById()
//        if (resultInfo.getCode() != ApiConstant.SUCCESS_CODE) {
//            throw new ParameterException(resultInfo.getMessage());
//        }
//        SignInUserInfo dinerInfo = BeanUtil.fillBeanWithMap((LinkedHashMap) resultInfo.getData(),
//                new SignInUserInfo(), false);
//        return dinerInfo;
//    }
//
//}
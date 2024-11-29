package com.jdyy.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.jdyy.commons.util.Result;
import com.jdyy.controller.MusicController;
import com.jdyy.entity.MusicList;
import com.jdyy.entity.User;
import com.jdyy.entity.vo.Page;
import com.jdyy.entity.vo.SignInUserInfo;
import com.jdyy.mapper.UserMapper;
import com.jdyy.service.UserService;
import io.swagger.models.auth.In;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 *
 * 用户 服务实现类
 *
 * @author LvXinming
 * @since 2022/10/13
 */

@Service

public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;

    //分页查询
    public Result getUserPage(Page<User> page){
        Result result;
        List<User> users;
        //从第几条开始，-1是因为页数是从1开始，而查询的数据是从0开始
        int dataStart = (page.getPageNum()-1)*page.getPageSize();
        int allDataSum = userMapper.countUser();//所有数据
        System.out.println("当前页数"+page.getPageNum()+" "+"当前一页的大小"+page.getPageSize());
        System.out.println("从第 "+dataStart+" 条开始");
        page.setDataStart(dataStart);
        page.setAllDataSum(allDataSum);
        try {
            users = userMapper.getUserPage(page);
            if (users == null){
                result = Result.success(204,"没有数据或已经是尾页数据",null);
            }else{
                page.setPageData(users);
                result = Result.success("获取分页正常",page);
            }
        }catch (Exception e){
            e.printStackTrace();//打印错误信息
            result = Result.fail("获取分页异常");
        }
        return result;
    }

    //查询所有用户
    @Override
    public Result findAll() {
        Result result;
        try {
            List<User> users = userMapper.getAll();
            result = Result.success(200,"成功获取所有用户",users);
        }catch (Exception e){
            e.printStackTrace();
            result = Result.fail("获取所有用户失败");
        }
        return result;
    }
    //根据Id获取用户
    @Override
    public Result getUserById(Integer uid) {
        Result result;
        try {
            SignInUserInfo user = userMapper.getUserById(uid);
            result = Result.success(200,"成功获取uid为"+uid+"的用户",user);
        }catch (Exception e){
            e.printStackTrace();
            result = Result.fail("获取用户失败");
        }
        return result;
    }

    //添加用户
    @Override
    public Result addUser(User user) {
        Result result;
        try {
            userMapper.addUser(user);
            result =  Result.success("添加成功");

        }catch (DuplicateKeyException e){
            result = Result.fail(406,"添加失败，用户已存在");
            userMapper.fixAutoincrement();
        }catch (Exception e){
            e.printStackTrace();
            result = Result.fail("添加失败");
            userMapper.fixAutoincrement();
        }
        return result;
    }

    //修改用户图片地址（内）
    public Result modifyUser(User user){
        Result result;
        try {
            System.out.println("@"+user);
            userMapper.modifyUser(user);
            result = Result.success("修改用户成功");
        }catch (Exception e){
            e.printStackTrace();
            result = Result.fail("修改用户失败");
        }
        return result;
    }


    //删除用户
    @Override
    public Result removeUser(User user) {
        Result result;
        User aUser = userMapper.getOneUser(user);
        try {
            if(aUser==null)
                return new Result(404,"删除失败，未找到此用户");
            userMapper.removeUser(user);
            result = Result.success("删除成功",null);
            //删除后ID号自增是否向前呢？这是个问题
            userMapper.fixAutoincrement();
        }catch (Exception e){
            e.printStackTrace();
            result = new Result(500,"删除失败");
        }
        return result;
    }

    //登录
    @Override
    public Result login(@RequestBody User userLogin) {
        Result result;
        try {
            User user = userMapper.login(userLogin);
            if (user!=null){
                StpUtil.login(user.getUid());
                System.out.println(StpUtil.getTokenInfo());
                System.out.println(StpUtil.getLoginId());
                System.out.println(StpUtil.getPermissionList());
                System.out.println(StpUtil.getRoleList());
                Map<String, Object> map = new HashMap<>();
                map.put("user",user);//添加用户信息到data
                map.put("token",StpUtil.getTokenInfo());//添加token到data
//                System.out.println(map.get("token"));
                result = new Result(200,"登录成功",map);
//                return result;
            }else {
                result = Result.fail(401,"用户名或密码错误");
//                return result;
            }
        }catch (Exception e){

            result = Result.fail("登录失败...");
            throw new RuntimeException();
        }
        return result;
    }

    //退出登录
    public Result logout(String tokenValue){
        System.out.println(StpUtil.getLoginIdByToken(tokenValue));
            if(StpUtil.isLogin()){
                StpUtil.logout(StpUtil.getLoginIdByToken(tokenValue));

                return Result.success(200,"退出成功",null);
            }else {
                return Result.fail("未登录状态");
            }


    }

    //注册
    @Transactional
    public Result register(User user,MultipartFile avatarFile) {
        Result result;
        int flag=0;
        try {
            System.out.println(user);
            userMapper.fixAutoincrement();
            flag= userMapper.addUser(user);

            if (avatarFile!=null){
                Result uploadResult = upload(avatarFile,user,1);
                System.out.println(uploadResult);
//                if(uploadResult.getCode()!=200){
//                    removeMusicList(musicList.getLid());
//                    musicListMapper.modifyAutoincrement(musicListMapper.getLid()-1);
//                    return uploadResult;
//                }
                String avatar = (String) uploadResult.getData();
                user.setAvatar(avatar);

            }

            modifyUser(user);
            result =  Result.success("注册成功");


        }catch (DuplicateKeyException e){
            result = Result.fail(409,"注册失败，用户已存在");
            userMapper.fixAutoincrement();
        }catch (Exception e){
            e.printStackTrace();
            result = Result.fail("注册失败");

            throw new RuntimeException();
        }finally {
            System.out.println(user.getUid());
            //如果插入成功
            if(flag>0) {
                //插入默认角色标识 user:1 admin:2
                List<Integer> rlists=new ArrayList<>();
                rlists.add(1);
                for(Integer rl:rlists) userMapper.insertRolesById(user.getUid(),rl);

                //插入默认权限标识
                List<Integer> plists=new ArrayList<>();
                plists.add(1);
                for(Integer pl:plists) userMapper.insertPermissionsById(user.getUid(),pl);
            }
        }


        return result;
    }


    //获取当前用户的角色标识
    public List<String> getRolesById(Object uid){
        System.out.println();
        System.out.println(StpUtil.getLoginId());
        return userMapper.getRolesById(uid);
    }

    //获取当前用户的权限标识
    public List<String> getPermissionsById(Object uid){
        System.out.println(StpUtil.getLoginId());
        return userMapper.getPermissionsById(uid);
    }

    //用户图片上传
    public Result upload(MultipartFile file, User user, int code) {//code 0为自判定 1为图片 2为音频
        Result result;
        String originFileName = file.getOriginalFilename();//原始文件名
        System.out.println(originFileName);
        if (file.isEmpty()){
            return Result.fail("文件不存在");
        }else if(originFileName==null){
            return Result.fail("文件名不能为空");
        }

        //路径处理
        String relativePath = MusicController.class.getClassLoader().getResource("").getPath();//获取绝对路径
        relativePath = URLDecoder.decode(relativePath, StandardCharsets.UTF_8);//处理字符问题
        System.out.println(relativePath);
        String savePath = "static/user/";//保存路径
        String path = relativePath+savePath;//拼接

        //文件后缀处理
        String fileSuffix = originFileName.substring(originFileName.lastIndexOf('.'));//文件后缀
        String[] supportImgSuffix = {".jpg",".png",".jpeg"};//支持的音乐封面图片后缀
        String[] supportAudioSuffix = {".mp3"};//支持的音频后缀
        //判断文件后缀

        //code为1，应为图片时; code为2，应为音频时
        if(code==1&&!Arrays.stream(supportImgSuffix).toList().contains(fileSuffix)){
            return Result.fail(406,"文件上传错误，文件类型应为图片",null);
        }else if(code==2&&!Arrays.stream(supportAudioSuffix).toList().contains(fileSuffix)){
            return Result.fail(406,"文件上传错误，文件类型应为音频",null);
        }

        boolean isImg = false;//是否为图片
        if (Arrays.stream(supportImgSuffix).toList().contains(fileSuffix)){
            isImg = true;
            path+="avatar/"+user.getUid();
        }else if(Arrays.stream(supportAudioSuffix).toList().contains(fileSuffix)){
            path+="audio/"+user.getUid();
        }else{
            return Result.fail(406,"不支持此文件",null);
        }

        //保存文件
        File realPath = new File(path);//创建文件夹
        if(!realPath.exists()){//文件夹不存在则创建文件夹
            realPath.mkdirs();
        }

        try {

            //改名
            String newFileName = originFileName;
            if (user!=null){
                newFileName = "uid_"+user.getUid()+fileSuffix;
            }

            file.transferTo(new File(path,newFileName));

            result = Result.success(200,"文件上传成功","user/"+(isImg?"avatar/"+user.getUid()+"/":"audio/"+user.getUid()+"/")+newFileName);
        } catch (IOException e) {
            e.printStackTrace();
            result = Result.fail("文件上传失败",e.getMessage());
        }
        return result;
    }


}

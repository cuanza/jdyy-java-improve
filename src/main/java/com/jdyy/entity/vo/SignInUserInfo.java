package com.jdyy.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

/**
 * @Author:陈镇川
 * @QQ:1026551395
 * @Date:2024/11/23 16:36
 **/

@Data
@ToString
public class SignInUserInfo {
    @Schema(description = "用户ID")
    private Integer uid;//用户 ID

    @Schema(description = "用户名")
    private String username; //用户名

    @Schema(description = "用户角色")
    private String role;//用户角色

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "用户注册时间")
    private Timestamp create_date;

    @Schema(description = "用户最近登录时间")
    private Timestamp login_date;

    @Schema(description = "用户修改信息时间")
    private Timestamp update_date;

    @Schema(description = "用户年龄")
    private String age;

    @Schema(description = "用户单位")
    private String unit;

    @Schema(description = "用户性别")
    private String gender;

    @Schema(description = "用户邮箱")
    private String email;

    //以下字段待增加（未完工）
    //    @Schema(description = "用户昵称")
//    private String nickname;

//    @Schema(description = "用户性别")
//    private String gender;
//
//    @Schema(description = "用户邮箱")
//    private String email;
//
//    @Schema(description = "用户手机号")
//    private String tel;
//
//    @Schema(description = "用户地区")
//    private String area;
//
//    @Schema(description = "用户个性签名")
//    private String signature;
}

package com.jdyy.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.models.auth.In;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

/**
 * @Author:陈镇川
 * @QQ:1026551395
 * @Date:2024/11/23 15:03
 **/

@ApiModel("关注实体类")
@Data
@ToString
public class Follow {
    @Schema(description = "列表ID")
    private Integer id;

    @Schema(description = "用户ID")
    private Integer userId;

    @Schema(description = "关注的用户ID")
    private Integer followUserId;

    @Schema(description = "关注状态，0-没有关注，1-关注了")
    private Integer isValid;

    @Schema(description = "创建时间")
    private Timestamp createDate;

    @Schema(description = "更新时间")
    private Timestamp updateDate;
}

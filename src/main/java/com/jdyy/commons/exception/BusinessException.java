package com.jdyy.commons.exception;


import com.jdyy.commons.util.ErrorEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * @Author:陈镇川
 * @QQ:1026551395
 * @Date:2024/11/23 16:15
 **/

/**
 * 业务异常
 */
@Getter
@Schema(name = "业务异常", description = "业务异常")
public class BusinessException extends RuntimeException {

    @Schema(description = "用户提示", example = "操作成功！")
    private final String userMessage;

    /**
     * 错误码<br>
     * 调用成功时，为 null。<br>
     * 示例：10001
     */
    @Schema(description = "错误码")
    private final String errorCode;

    /**
     * 错误信息<br>
     * 调用成功时，为 null。<br>
     * 示例："验证码无效"
     */
    @Schema(description = "错误信息")
    private final String errorMessage;


    public BusinessException(ErrorEnum errorEnum) {
        super(String.format("错误码：[%s]，错误信息：[%s]，用户提示：[%s]", errorEnum.name(), errorEnum.getMessage(), errorEnum.getMessage()));
        this.userMessage = errorEnum.getMessage();
        this.errorCode = errorEnum.name();
        this.errorMessage = errorEnum.getMessage();
    }


    public BusinessException(String userMessage, String errorCode, String errorMessage) {
        super(String.format("错误码：[%s]，错误信息：[%s]，用户提示：[%s]", errorCode, errorMessage, userMessage));
        this.userMessage = userMessage;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}


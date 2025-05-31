package com.wj.bookstore.common.vo;

import com.wj.bookstore.common.enums.StatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-14-11:36
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Status {
    @ApiModelProperty(value = "状态码, 0表示成功返回, 其他异常返回",required = true,example = "0")
    private int code;

    @ApiModelProperty(value = "正确返回ok, 异常时为描述文案",required = true,example = "ok")
    private String msg;

    public static Status newStatus(int code, String msg) {return new Status(code,msg);}

    public static Status newStatus(StatusEnum status, Object... msgs){
        String msg;
        if(msgs.length>0){
            msg=String.format(status.getMsg(),msgs);
        }else{
            msg=status.getMsg();
        }
        return newStatus(status.getCode(),msg);
    }
}

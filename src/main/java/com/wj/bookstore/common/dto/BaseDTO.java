package com.wj.bookstore.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-17-18:09
 **/
@Data
public class BaseDTO {
    @ApiModelProperty(value = "业务主键")
    private Long id;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "最后编辑时间")
    private String updateTime;
}

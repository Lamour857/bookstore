package com.wj.bookstore.common.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-09-19:17
 **/
@Data
@ApiModel(description = "上传图片回调参数")
public class UploadImageNotify {

    @NotNull(message = "success不能为空")
    @ApiModelProperty(value = "是否成功")
    private Boolean success;

    @NotNull(message = "urlList不能为空")
    @ApiModelProperty(value = "url列表")
    private List<String> urlList;

    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "图片对应主键")
    private Long id;
}

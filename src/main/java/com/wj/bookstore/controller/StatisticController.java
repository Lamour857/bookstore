package com.wj.bookstore.controller;

import com.wj.bookstore.common.vo.ResVo;
import com.wj.bookstore.statistic.entity.DTO.MerchantStatisticDTO;
import com.wj.bookstore.statistic.service.StatisticService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-31-16:23
 **/
@RestController
@RequestMapping("/statistic")
@Api(tags = "统计管理")
public class StatisticController {
    @Autowired
    private StatisticService statisticService;

    @GetMapping("/merchant")
    @ApiOperation(value = "获取店铺统计信息")
    public ResVo<MerchantStatisticDTO> getMerchantStatistics(
            @ApiParam(value = "店铺ID",required = true)
            @NotNull(message = "店铺ID不能为空")
            @RequestParam(name="merchantId") Long merchantId) {
        return ResVo.ok(statisticService.getMerchantStatistics(merchantId));
    }
}

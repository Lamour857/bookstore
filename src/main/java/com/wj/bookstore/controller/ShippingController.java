package com.wj.bookstore.controller;

import com.wj.bookstore.common.vo.ResVo;
import com.wj.bookstore.delivery.shipping.entity.ShippingDTO;
import com.wj.bookstore.delivery.shipping.entity.ShippingInfoReq;
import com.wj.bookstore.delivery.shipping.service.ShippingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-12-16:14
 **/
@RestController
@Slf4j
@Api(tags = "物流信息管理")
@RequestMapping("/shipping")
public class ShippingController {
    @Autowired
    private ShippingService shippingService;
    @PostMapping("/update")
    public ResVo<String> update(@RequestBody ShippingInfoReq req) {
        shippingService.updateShippingInfo(req);
        return ResVo.ok();
    }

    @GetMapping("/info")
    public ResVo<ShippingDTO> getShippingInfo(
            @ApiParam(value = "物流信息ID", required = true)
            @NotNull(message = "物流信息ID不能为空")
            @RequestParam("shippingId") Long id) {
        return ResVo.ok(shippingService.getShippingInfo(id));
    }
}

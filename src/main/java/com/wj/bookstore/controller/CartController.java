package com.wj.bookstore.controller;

import com.alipay.api.AlipayApiException;

import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.wj.bookstore.common.validation.CheckoutGroup;
import com.wj.bookstore.common.validation.CreateCartItemGroup;
import com.wj.bookstore.common.vo.ResVo;
import com.wj.bookstore.payment.alipay.config.AlipayConfig;
import com.wj.bookstore.payment.alipay.service.AlipayService;
import com.wj.bookstore.shopping.cart.entity.req.CartItemReq;
import com.wj.bookstore.shopping.cart.entity.req.CheckoutReq;
import com.wj.bookstore.shopping.cart.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-06-14:40
 **/
@RestController
@Slf4j
@Api(tags = "购物车管理")
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private AlipayConfig alipayConfig;
    @Autowired
    private AlipayService alipayService;
    @PreAuthorize("hasAuthority('CartManage')")
    @PostMapping("/add")
    @ApiOperation(value = "添加购物车条目")
    public ResVo<String> addCartItem(
            @Validated(CreateCartItemGroup.class)
            @RequestBody CartItemReq req) {
        cartService.addCartItem(req);
        return ResVo.ok();
    }
    @PreAuthorize("hasAuthority('CartManage')")
    @GetMapping("/list")
    @ApiOperation(value = "获取购物车条目列表")
    public ResVo<Object> getCartItemList() {
        return ResVo.ok(cartService.getCartItemList());
    }

    @PreAuthorize("hasAuthority('CartManage')")
    @PostMapping("/checkout")
    @ApiOperation(value = "结账")
    public ResVo<String> checkout(
            @Validated(CheckoutGroup.class)
            @RequestBody CheckoutReq req) throws JSONException {
        Map<String,String> orderInfo= cartService.checkout(req);
        // 创建client
        String form =alipayService.pay(orderInfo.get("orderId"),new BigDecimal(orderInfo.get("totalAmount")),orderInfo.get("subject"));
        return ResVo.ok(form);
    }

    @PreAuthorize("hasAuthority('CartManage')")
    @PostMapping("/freight")
    @ApiOperation(value = "获取价格")
    public ResVo<BigDecimal> getFreight(
            @Validated(CheckoutGroup.class)
            @RequestBody CheckoutReq req) {
        return ResVo.ok(cartService.getFreight(req));
    }


    @PreAuthorize("hasAuthority('CartManage')")
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除购物车条目")
    public ResVo<String> deleteCartItem(
            @NotNull(message = "cartItemId不能为空")
            @RequestParam Long cartItemId) {
        cartService.deleteCartItem(cartItemId);
        return ResVo.ok();
    }
}

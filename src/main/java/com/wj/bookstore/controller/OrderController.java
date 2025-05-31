package com.wj.bookstore.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.wj.bookstore.common.enums.StatusEnum;
import com.wj.bookstore.common.utils.ExceptionUtil;
import com.wj.bookstore.common.validation.CreateRefundReqGroup;
import com.wj.bookstore.common.validation.DeliveryShippingInfoGroup;
import com.wj.bookstore.common.vo.PageResult;
import com.wj.bookstore.common.vo.ResVo;
import com.wj.bookstore.common.vo.req.UploadImageNotify;
import com.wj.bookstore.delivery.shipping.entity.ShippingInfoReq;
import com.wj.bookstore.payment.alipay.config.AlipayConfig;
import com.wj.bookstore.shopping.order.entity.DO.OrderDO;
import com.wj.bookstore.shopping.order.entity.DTO.OrderDTO;
import com.wj.bookstore.shopping.order.entity.DTO.OrderItemDTO;
import com.wj.bookstore.shopping.order.entity.DTO.RefundQueryDTO;
import com.wj.bookstore.shopping.order.entity.DTO.RefundReqDTO;
import com.wj.bookstore.shopping.order.entity.req.*;
import com.wj.bookstore.shopping.order.repository.dao.OrderDao;
import com.wj.bookstore.shopping.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-06-11:52
 **/
@RestController
@Slf4j
@Api(tags = "订单管理")
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PreAuthorize("hasAuthority('ConsumerOrderManage')")
    @ApiOperation(value = "订单列表")
    @PostMapping("/user/page")
    public ResVo<PageResult<OrderDTO>> list(
            @ApiParam(value = "订单查询请求参数",required = true)
            @RequestBody QueryOrderReq req) {
        return ResVo.ok(orderService.userPageQuery(req));
    }

    @PreAuthorize("hasAuthority('ConsumerOrderManage')")
    @ApiOperation(value = "订单条目列表")
    @PostMapping("/user/item/page")
    public ResVo<PageResult<OrderItemDTO>> userOrderItemList(
            @ApiParam(value = "订单查询请求参数",required = true)
            @RequestBody QueryOrderReq req) {
        return ResVo.ok(orderService.userOrderItemPageQuery(req));
    }

//    @PreAuthorize("hasAuthority('MerchantOrderManage')")
//    @ApiOperation(value = "商家订单列表")
//    @PostMapping("/merchant/page")
//    public ResVo<PageResult<OrderItemDTO>> merchantList(@RequestBody QueryOrderReq req) {
//        return ResVo.ok(orderService.merchantPageQuery(req));
//    }

    @PreAuthorize("hasAuthority('MerchantOrderManage')")
    @ApiOperation(value = "商家订单列表")
    @PostMapping("/merchant/query")
    public ResVo<PageResult<OrderItemDTO>> queryMerchantOrder(
            @ApiParam(value = "订单状态",required = true)
            @RequestBody QueryOrderReq req) {
        return ResVo.ok(orderService.merchantPageQuery(req));
    }

    @PreAuthorize("hasAuthority('MerchantOrderManage')")
    @ApiOperation(value = "订单发货")
    @PostMapping("/delivery")
    public ResVo<String> delivery(
            @ApiParam(value = "物流信息",required = true)
            @Validated(DeliveryShippingInfoGroup.class)
            @RequestBody ShippingInfoReq req) {
        orderService.delivery(req);
        return ResVo.ok();
    }

    @PreAuthorize("hasAuthority('ConsumerOrderManage')")
    @ApiOperation(value = "确认收货")
    @GetMapping("/receipt")
    public ResVo<String> receipt(
            @ApiParam(value = "订单条目id",required = true)
            @NotNull(message = "订单不能为空")
            @RequestParam Long orderItemId) {
        orderService.receipt(orderItemId);
        return ResVo.ok();
    }

    @PreAuthorize("hasAuthority('ConsumerOrderManage')")
    @ApiOperation(value = "取消订单")
    @PostMapping("/cancel")
    public ResVo<String> cancel(
            @ApiParam(value = "取消订单请求参数",required = true)
            @Valid
            @RequestBody CancelReq req) {
        if(req.getIsPaid()==null){
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"isPaid不能为空");
        }
        if(req.getIsPaid()){
            orderService.cancelPaid(req);
        }else{
            orderService.cancelUnPaid(req);
        }
        return ResVo.ok();
    }


    @PreAuthorize("hasAuthority('ConsumerOrderManage')")
    @ApiOperation(value = "订单退款")
    @PostMapping("/refund")
    public ResVo<String> refund(
            @ApiParam(value = "订单退款请求参数",required = true)
            @Validated(CreateRefundReqGroup.class)
            @RequestBody RefundReq req) {
        return ResVo.ok(orderService.refund(req));
    }

    @PreAuthorize("hasAuthority('ConsumerOrderManage')")
    @ApiOperation(value = "订单退款请求更新")
    @PostMapping("/refund/update")
    public ResVo<String> refundUpdate(@RequestBody RefundReq req){

        return ResVo.ok(orderService.updateRefund(req));
    }

    @PreAuthorize("hasAuthority('CommonOrderManage')")
    @ApiOperation(value = "获取订单退货信息")
    @GetMapping("/refund")
    public ResVo<RefundReqDTO> refundInfo(
            @ApiParam(value = "订单条目id",required = true)
            @NotNull(message = "orderItemId不能为空")
            @RequestParam("orderItemId") Long orderItemId) {
        return ResVo.ok(orderService.getRefundInfo(orderItemId));
    }

    @PreAuthorize("hasAuthority('CommonOrderManage')")
    @ApiOperation(value = "查询订单退款状态")
    @GetMapping("/refund/query")
    public ResVo<String> refundQuery(
            @ApiParam(value = "订单条目id",required = true)
            @NotNull(message = "订单条目Id不能为空")
            @RequestParam("orderItemId") Long orderItemId) {

        return ResVo.ok(orderService.refundQuery(orderItemId));
    }

    @PreAuthorize("hasAuthority('RefundingManage')")
    @ApiOperation(value = "获取申诉退款列表")
    @GetMapping("/refund/appealing")
    public ResVo<PageResult<RefundQueryDTO>> getRefundList(
            @ApiParam(value = "页码",required = true)
            @RequestParam int pageNum,
            @ApiParam(value = "页大小",required = true)
            @RequestParam int pageSize) {
        return ResVo.ok(orderService.getAppealingRefundList(pageNum,pageSize));
    }



    @PostMapping("/refund/notify")
    @ApiOperation(value = "订单退货上传图片回调")
    public ResVo<String> notify(
            @ApiParam(value = "上传图片回调参数",required = true)
            @Valid
            @RequestBody UploadImageNotify notify){
        if(notify.getSuccess()){
            orderService.saveImageUrl( notify.getUrlList(),notify.getId(),notify.getId());
        }
        return ResVo.ok("success");
    }

    @PreAuthorize("hasAuthority('RefundingManage')")
    @ApiOperation(value = "管理员处理退款申诉")
    @PutMapping("/appeal/handle")
    public ResVo<String> handleAppeal(
            @ApiParam(value = "退款记录号",required = true)
            @NotNull(message = "refundNumber不能为空")
            @RequestParam String refundNumber,
            @ApiParam(value = "是否同意",required = true)
            @NotNull(message = "isAgree不能为空")
            @RequestParam
            Boolean isAgree) {

        orderService.handleAppeal(refundNumber, isAgree);

        return ResVo.ok();
    }

    @PreAuthorize("hasAuthority('MerchantOrderManage')")
    @PostMapping("/handleRefund")
    public ResVo<String> refund(
            @ApiParam(value = "处理退款请求参数",required = true)
            @Valid
            @RequestBody HandleRefundReq req) throws JSONException {

        Boolean result = orderService.handleRefund(req);
        if(!result){
            return ResVo.fail(StatusEnum.UNEXPECT_ERROR,"退款失败");
        }
        return ResVo.ok();
    }

    @PreAuthorize("hasAuthority('ConsumerOrderManage')")
    @ApiOperation(value = "支付未支付订单")
    @GetMapping("/pay")
    public ResVo<String> pay(
            @ApiParam(value = "订单id",required = true)
            @NotNull(message = "订单id不能为空")
            @RequestParam String orderId)  {
        String form=orderService.pay(orderId);
        return ResVo.ok(form);
    }

    @PreAuthorize("hasAuthority('ConsumerOrderManage')")
    @ApiOperation(value = "订单申诉")
    @PostMapping("/appeal")
    public ResVo<String> appeal(
            @ApiParam(value = "退款记录号",required = true)
            @NotNull(message = "退款记录号不能为空")
            @RequestParam String refundNumber) {
        orderService.appeal(refundNumber);
        return ResVo.ok();
    }

    @PreAuthorize("hasAuthority('MerchantOrderManage')")
    @ApiOperation(value = "商家同意退货并给出收货信息")
    @PostMapping("/agreeRefund")
    public ResVo<String> agreeRefund(
            @ApiParam(value = "退货信息",required = true)
            @Valid
            @RequestBody ReturnShippingInfoReq req) {

        orderService.agreeRefund(req);
        return ResVo.ok();
    }

    @PreAuthorize("hasAuthority('ConsumerOrderManage')")
    @ApiOperation(value = "订单退货")
    @PostMapping("/returnGoods")
    public ResVo<String> returnGoods(
            @ApiParam(value = "物流信息",required = true)
            @RequestBody ShippingInfoReq req) {
        orderService.returnGoods(req);
        return ResVo.ok();
    }

}

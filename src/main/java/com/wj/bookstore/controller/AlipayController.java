package com.wj.bookstore.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.wj.bookstore.payment.alipay.config.AlipayConfig;
import com.wj.bookstore.shopping.order.service.OrderService;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-07-18:13
 **/
@RestController
@RequestMapping("/alipay")
@Slf4j
public class AlipayController {
    @Autowired
    private AlipayConfig alipayConfig;
    @Autowired
    private OrderService orderService;
    @ApiOperation(value = "支付宝支付回调接口")
    @PostMapping("/pay/notify")
    public void payNotify(HttpServletRequest request) throws AlipayApiException {
        if(request.getParameter("trade_status").equals("TRADE_SUCCESS")){
            Map<String,String> params = new HashMap<>();
            Map<String,String[]> requestParams = request.getParameterMap();

            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
            }
            String sign=request.getParameter("sign");
            String content= AlipaySignature.getSignCheckContentV1(params);
            boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, alipayConfig.getPublicKey(), alipayConfig.getCharset());

            if(checkSignature){
                orderService.notifyPaid(params);
            }
        }
    }
//    @ApiOperation(value = "支付宝退款回调接口")
//    @PostMapping("/refund/notify")
//    public void refundNotify(HttpServletRequest request) throws AlipayApiException {
//        if(request.getParameter("refund_status").equals("SUCCESS")){
//            Map<String,String> params = new HashMap<>();
//            Map<String,String[]> requestParams = request.getParameterMap();
//
//            for (String name : requestParams.keySet()) {
//                params.put(name, request.getParameter(name));
//            }
//            String sign=request.getParameter("sign");
//            String content= AlipaySignature.getSignCheckContentV1(params);
//            boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, alipayConfig.getPublicKey(), alipayConfig.getCharset());
//
//            if(checkSignature){
//                orderService.notifyRefund(params);
//            }
//        }
//    }
}

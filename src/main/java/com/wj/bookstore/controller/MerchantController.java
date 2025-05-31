package com.wj.bookstore.controller;

import com.wj.bookstore.common.validation.CreateWithdrawalReqGroup;
import com.wj.bookstore.common.validation.UpdateMerchantReqGroup;
import com.wj.bookstore.common.validation.UpdateWithdrawalReqGroup;
import com.wj.bookstore.common.vo.PageResult;
import com.wj.bookstore.common.vo.ResVo;
import com.wj.bookstore.common.vo.req.UploadImageNotify;
import com.wj.bookstore.user.merchant.entity.DTO.MerchantDTO;
import com.wj.bookstore.user.merchant.entity.DTO.WithdrawalRequestDTO;
import com.wj.bookstore.user.merchant.entity.req.QueryWithdrawalReq;
import com.wj.bookstore.user.merchant.entity.req.SaveMerchantReq;
import com.wj.bookstore.user.merchant.entity.req.WithdrawalReq;
import com.wj.bookstore.user.merchant.entity.req.QueryMerchantReq;
import com.wj.bookstore.user.merchant.service.MerchantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apiguardian.api.API;
import org.json.JSONException;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-09-16:13
 **/
@RestController
@Slf4j
@RequestMapping("/merchant")
@Api(tags = "店铺管理")
public class MerchantController {
    @Autowired
    private MerchantService merchantService;

    @GetMapping()
    @ApiOperation(value = "获取店铺信息")
    public ResVo<MerchantDTO> getById(
            @ApiParam(value = "店铺id",required = true)
            @NotNull(message = "店铺id不能为空")
            @RequestParam(name="merchantId") Long merchantId){
        return ResVo.ok(merchantService.getById(merchantId));
    }

    @PostMapping("/withdrawal")
    @ApiOperation(value = "提现")
    public ResVo<String> draw(
            @ApiParam(value = "提现请求",required = true)
            @Validated(CreateWithdrawalReqGroup.class)
            @RequestBody WithdrawalReq req)  {
        merchantService.withdrawal(req);
        return ResVo.ok();
    }
    @PostMapping("/withdrawal/query")
    @ApiOperation(value = "管理员查询提现请求")
    public ResVo<PageResult<WithdrawalRequestDTO>> queryWithdrawal(
            @ApiParam(value = "查询提现请求",required = true)
            @RequestBody QueryWithdrawalReq req){
        return ResVo.ok(merchantService.queryWithdrawalRequest(req));
    }

    @PostMapping("/withdrawal/update")
    @ApiOperation(value = "提现请求更新")
    public ResVo<String> updateWithdrawal(
            @ApiParam(value = "提现请求",required = true)
            @Validated(UpdateWithdrawalReqGroup.class)
            @RequestBody WithdrawalReq req){
        merchantService.updateWithdrawal(req);
        return ResVo.ok();
    }

    @GetMapping("/withdrawal/list")
    @ApiOperation(value = "商家查询提现请求")
    public ResVo<PageResult<WithdrawalRequestDTO>> getWithdrawalList(
            @ApiParam(value = "商家id",required = true)
            @RequestParam(name="merchantId")
            @NotNull(message = "商家id不能为空") Long merchantId,
            @ApiParam(value = "页码",required = true)
            @NotNull(message = "页码不能为空")
            @RequestParam(name="pageNum") int pageNum,
            @ApiParam(value = "页大小",required = true)
            @NotNull(message = "页大小不能为空")
            @RequestParam(name="pageSize") int pageSize){
        return ResVo.ok(merchantService.getWithdrawalList(merchantId, pageNum, pageSize));
    }

    @PutMapping("/withdrawal/handle")
    @ApiOperation(value = "管理员处理提现请求")
    public ResVo<String> handleWithdrawal(
            @ApiParam(value = "提现请求id",required = true)
            @NotNull(message = "提现请求id不能为空")
            @RequestParam(name="withdrawalId") String withdrawalId,
            @ApiParam(value = "是否同意",required = true)
            @NotNull(message = "是否同意不能为空")
            @RequestParam(name="isAgree") Boolean isAgree) {

        merchantService.handleWithdrawal(withdrawalId,isAgree);

        return ResVo.ok();
    }

    @PostMapping("/query")
    @ApiOperation(value = "分页查询店铺")
    public ResVo<PageResult<MerchantDTO>> queryMerchant(
            @ApiParam(value = "查询店铺请求",required = true)
            @RequestBody QueryMerchantReq req){
        return ResVo.ok(merchantService.queryMerchant(req));
    }


    @PostMapping("/add")
    @ApiOperation(value = "添加店铺")
    public ResVo<Map<String,String>> addStore(
            @RequestBody SaveMerchantReq saveMerchantReq){
        return ResVo.ok(merchantService.addStore(saveMerchantReq));
    }

    @PostMapping("/notify")
    @ApiOperation(value = "上传图片回调")
    public ResVo<String> notify(
            @Valid @RequestBody UploadImageNotify notify){
        if(notify.getSuccess()){
            merchantService.saveImageUrl( notify.getUrlList());
        }
        return ResVo.ok("success");
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新店铺")
    public ResVo<Map<String,String>> updateStore(
            @Validated(UpdateMerchantReqGroup.class)
            @RequestBody SaveMerchantReq req){
        return ResVo.ok(merchantService.updateStore(req));
    }

//    @GetMapping("/list")
//    @ApiOperation(value = "获取店铺列表")
//    public ResVo<PageResult<MerchantDTO>> getStoreList(
//            @ApiParam(value = "页码",required = true)
//            @RequestParam(name="pageNum", defaultValue = "0",required = false) int pageNum,
//            @ApiParam(value = "页大小",required = true)
//            @RequestParam(name="pageSize", defaultValue = "10",required = false) int pageSize){
//        return ResVo.ok( merchantService.getStoreList(pageNum,pageSize));
//    }

    @PostMapping("/status")
    @ApiOperation(value = "更新店铺状态")
    public ResVo<String> updateStoreState(
            @ApiParam(value = "店铺id",required = true)
            @NotNull(message = "店铺id不能为空")
            @RequestParam(name="storeId") Long storeId,
            @ApiParam(value = "店铺状态",required = true)
            @NotNull(message = "店铺状态不能为空")
            @RequestParam(name="status") String status) {
        merchantService.updateStoreState(storeId, status);
        return ResVo.ok();
    }
    
}

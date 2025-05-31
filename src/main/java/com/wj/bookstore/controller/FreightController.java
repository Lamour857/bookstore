package com.wj.bookstore.controller;

import com.wj.bookstore.common.vo.ResVo;
import com.wj.bookstore.delivery.template.entity.DTO.ShippingTemplateDTO;
import com.wj.bookstore.delivery.template.entity.req.FreightTemplateReq;
import com.wj.bookstore.delivery.template.entity.valid.DefaultFreightTemplateGroup;
import com.wj.bookstore.delivery.template.service.AreaService;
import com.wj.bookstore.delivery.template.service.FreightService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-02-18-19:59
 **/
@RestController
@Slf4j
@Api(tags = "物流管理")
@RequestMapping("/freight")
public class FreightController {
    @Autowired
    private AreaService areaService;
    @Autowired
    private FreightService  freightService;

    @PreAuthorize("hasAuthority('FreightTemplateManage')")
    @PostMapping("/template/add")
    @ApiOperation(value = "添加运费模板")
    public ResVo<String> addTemplate(
            @ApiParam(value = "运费模板请求对象",required = true)
            @Valid
            @RequestBody FreightTemplateReq req) {
        freightService.addTemplate(req);
        return ResVo.ok();
    }

    @PreAuthorize("hasAuthority('FreightTemplateManage')")
    @PostMapping("/template/update")
    @ApiOperation(value = "更新运费模板")
    public ResVo<String> updateTemplate(
            @ApiParam(value = "运费模板请求对象",required = true)
            @Validated(DefaultFreightTemplateGroup.class)
            @RequestBody FreightTemplateReq req) {
        freightService.updateTemplate(req);
        return ResVo.ok();
    }

    @PreAuthorize("hasAuthority('FreightTemplateManage')")
    @PostMapping("/template/delete")
    @ApiOperation(value = "删除运费模板")
    public ResVo<String> deleteTemplate(
            @ApiParam(value = "运费模板请求对象",required = true)
            @Valid
            @RequestBody FreightTemplateReq req) {
        freightService.deleteTemplate(req);
        return ResVo.ok();
    }

    @PreAuthorize("hasAuthority('FreightTemplateManage')")
    @GetMapping("/template/list")
    @ApiOperation(value = "获取运费模板列表")
    public ResVo<List<ShippingTemplateDTO>> freight(@RequestParam Long merchantId){
        return ResVo.ok(freightService.getShippingTemplateList(merchantId));
    }


    @PreAuthorize("hasAuthority('FreightTemplateManage')")
    @GetMapping("/template/briefList")
    @ApiOperation(value = "获取简要运费模板")
    public ResVo<List<ShippingTemplateDTO>> briefList(Long merchantId){
        return ResVo.ok(freightService.getBriefTemplateList(merchantId));
    }


}

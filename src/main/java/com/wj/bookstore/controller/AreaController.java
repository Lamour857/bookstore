package com.wj.bookstore.controller;

import com.wj.bookstore.common.vo.ResVo;
import com.wj.bookstore.delivery.template.entity.DTO.AreaDTO;
import com.wj.bookstore.delivery.template.entity.DTO.AreaTypeDTO;
import com.wj.bookstore.delivery.template.service.AreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-02-20-20:52
 **/
@RestController
@Slf4j
@Api(tags = "获取地区信息")
@RequestMapping("/admin/area")
public class AreaController {

    @Autowired
    private AreaService areaService;
//    @GetMapping("/top")
//    @ApiOperation(value = "获取顶级地区")
//    public ResVo<List<AreaDTO>> top(@ApiParam(value = "顶级地区类型",required = true)
//                                        @RequestParam(name="type") Integer type){
//        return ResVo.ok(areaService.getTopArea(type));
//    }
    @GetMapping("/tree")
    @ApiOperation(value = "获取地区树")
    public ResVo<List<AreaDTO>> tree(
            @ApiParam(value = "是否获取县级地区，0：不获取;1:获取",required = true)
            @RequestParam(name="withCounty")
            @NotNull(message = "withCounty不能为空")
            Integer withCounty){
        return ResVo.ok(areaService.getAreaTree(withCounty));
    }
    @GetMapping("/group")
    @ApiOperation(value = "获取分类好的地区列表")
    public ResVo<List<List<AreaDTO>>> groupArea(
            @ApiParam(value = "是否获取县级地区，0：不获取;1:获取",required = true)
            @RequestParam(name="withCounty")
            @NotNull(message = "withCounty不能为空")
            Integer withCounty){
        return ResVo.ok(areaService.getGroupArea(withCounty));
    }
    @GetMapping("/city")
    @ApiOperation(value = "获取地区树")
    public ResVo<List<AreaDTO>> cities(
            @ApiParam(value = "省级地区id",required = true)
            @RequestParam(name="id")
            @NotNull(message = "id不能为空")
            Short id) {
        return ResVo.ok(areaService.getCities(id));
    }

    @GetMapping("/type")
    @ApiOperation(value = "获取地区类型列表")
    public ResVo<List<AreaTypeDTO>> type(){
        return ResVo.ok(areaService.getAreaTypeList());
    }
}

package com.wj.bookstore.controller;

import com.wj.bookstore.common.enums.StatusEnum;
import com.wj.bookstore.common.vo.PageResult;
import com.wj.bookstore.common.vo.ResVo;
import com.wj.bookstore.product.category.entity.CategoryDTO;
import com.wj.bookstore.product.category.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-02-05-12:57
 **/
@RestController
@Slf4j
@Api(tags = "分类管理")
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/tree")
    @ApiOperation(value = "分类树")
    public ResVo<List<CategoryDTO>> tree(){
        return ResVo.ok(categoryService.getCategoryTree());
    }

    @PreAuthorize("hasAuthority('CategoryManage')")
    @GetMapping("/page")
    @ApiOperation(value = "分类分页列表")
    public ResVo<PageResult<CategoryDTO>> page(
            @ApiParam(value = "页码",required = true)
            @NotNull(message = "页码不能为空")
            @RequestParam(name="pageNum", defaultValue = "0",required = false) int pageNum,
            @ApiParam(value = "页大小",required = true)
            @NotNull(message = "页大小不能为空")
            @RequestParam(name="pageSize", defaultValue = "10",required = false) int pageSize
    ){
        return ResVo.ok(categoryService.list(pageNum, pageSize));
    }

    @PreAuthorize("hasAuthority('CategoryManage')")
    @PostMapping("/sub")
    @ApiOperation(value = "子分类列表")
    public ResVo<List<CategoryDTO>> sub(
            @ApiParam(value = "分类id",required = true)
            @NotNull(message = "分类id不能为空")
            @RequestParam(name="id") Long id){
        return ResVo.ok(categoryService.sub(id));
    }
    @PreAuthorize("hasAuthority('CategoryManage')")
    @PostMapping("/add")
    @ApiOperation(value = "增添分类")
    public ResVo<Boolean> add(
            @ApiParam(value = "父分类id",required = true)
            @NotNull(message = "父分类id不能为空")
            @RequestParam(name="parentId") Long parentId,
            @ApiParam(value = "分类名称",required = true)
            @NotNull(message = "分类名称不能为空")
            @RequestParam(name="name") String name) {
        return ResVo.ok(categoryService.add(parentId, name));
    }
    @PreAuthorize("hasAuthority('CategoryManage')")
    @PostMapping("/delete")
    @ApiOperation(value = "删除分类")
    public ResVo<Boolean> delete(
            @ApiParam(value = "分类id",required = true)
            @NotNull(message = "分类id不能为空")
            @RequestParam(name="id") Long id){
        if(id==0){
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"分类id不能为0");
        }
        return ResVo.ok(categoryService.delete(id));
    }
}

package com.wj.bookstore.controller;

import com.wj.bookstore.common.vo.PageResult;
import com.wj.bookstore.common.vo.ResVo;
import com.wj.bookstore.product.book.entity.DTO.BookBriefDTO;
import com.wj.bookstore.product.collection.service.CollectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-06-12:48
 **/
@RestController
@Slf4j
@Api(tags = "收藏管理")
@RequestMapping("/collection")
public class CollectionController {
    @Autowired
    private CollectionService collectionService;
    @PreAuthorize("hasAuthority('CollectionManage')")
    @PostMapping("/add")
    @ApiOperation(value = "收藏书籍")
    public ResVo<String> collect(
            @ApiParam(value = "书籍id",required = true)
            @NotNull(message = "书籍id不能为空")
            @RequestParam Long bookId) {
        collectionService.collect(bookId);
        return ResVo.ok();
    }

    @PreAuthorize("hasAuthority('CollectionManage')")
    @GetMapping("/page")
    @ApiOperation(value = "分页获取收藏书籍")
    public ResVo<PageResult<BookBriefDTO>> page(
            @ApiParam(value = "页码",required = true)
            @NotNull(message = "页码不能为空")
            @RequestParam Integer pageNum,
            @ApiParam(value = "页大小",required = true)
            @NotNull(message = "页大小不能为空")
            @RequestParam Integer pageSize) {
        PageResult<BookBriefDTO> page=collectionService.page(pageNum,pageSize);
        return ResVo.ok(page);
    }

    @PreAuthorize("hasAuthority('CollectionManage')")
    @PostMapping("/cancel")
    @ApiOperation(value = "取消收藏书籍")
    public ResVo<String> cancel(
            @ApiParam(value = "书籍id",required = true)
            @NotNull(message = "书籍id不能为空")
            @RequestParam Long bookId) {
        collectionService.cancel(bookId);
        return ResVo.ok();
    }
    @GetMapping("/judge")
    @ApiOperation(value = "判断书籍是否被收藏")
    public ResVo<Boolean> judge(
            @ApiParam(value = "书籍id",required = true)
            @NotNull(message = "书籍id不能为空")
            @RequestParam Long bookId) {
        Boolean isCollected=collectionService.judge(bookId);
        return ResVo.ok(isCollected);
    }
}

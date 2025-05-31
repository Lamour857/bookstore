package com.wj.bookstore.controller;

import com.wj.bookstore.common.vo.ResVo;
import com.wj.bookstore.product.book.entity.DTO.BookBriefDTO;
import com.wj.bookstore.product.book.entity.DTO.BookDTO;
import com.wj.bookstore.product.book.service.BookService;
import com.wj.bookstore.product.category.entity.CategoryDTO;
import com.wj.bookstore.product.category.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-05-13:53
 **/
@RestController
@Slf4j
@Api(tags ="公共接口")
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BookService bookService;
    @GetMapping("/category/tree")
    @ApiOperation(value = "分类树")
    public ResVo<List<CategoryDTO>> tree(){
        return ResVo.ok(categoryService.getCategoryTree());
    }

    @GetMapping("/book/new")
    @ApiOperation(value = "最新书籍")
    public ResVo<List<BookBriefDTO>> newBook(){
        return ResVo.ok(bookService.getLatestBookList());
    }

    @GetMapping("/book/hot")
    @ApiOperation(value = "热门书籍")
    public ResVo<List<BookBriefDTO>> hotBook(){
        return ResVo.ok(bookService.getHotSellingBookList());
    }

    @GetMapping("/book")
    @ApiOperation(value = "书籍信息")
    public ResVo<BookDTO> book(
            @ApiParam(value = "书籍id",required = true)
            @NotNull(message = "书籍id不能为空")
            @RequestParam Long bookId){
        return ResVo.ok(bookService.getById(bookId));
    }

}

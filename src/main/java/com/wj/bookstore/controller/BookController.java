package com.wj.bookstore.controller;

import com.wj.bookstore.common.validation.CreateBookGroup;
import com.wj.bookstore.common.validation.UpdateBookGroup;
import com.wj.bookstore.common.vo.PageResult;
import com.wj.bookstore.common.vo.ResVo;
import com.wj.bookstore.core.oss.service.OssService;
import com.wj.bookstore.product.book.entity.DTO.BookBriefDTO;
import com.wj.bookstore.product.book.entity.DTO.BookDTO;
import com.wj.bookstore.product.book.entity.DTO.BookAgeDTO;
import com.wj.bookstore.product.book.entity.req.BookCreateReq;
import com.wj.bookstore.product.book.entity.req.QueryBookReq;
import com.wj.bookstore.product.book.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-02-17-10:36
 **/
@RestController
@Slf4j
@Api(tags = "书籍管理")
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private OssService ossService;
    @PreAuthorize("hasAuthority('BookManage')")
    @GetMapping("/age")
    @ApiOperation(value = "出版年代树")
    public ResVo<List<BookAgeDTO>> tree(){
        return ResVo.ok(bookService.getBookAgeTree());
    }


    @PreAuthorize("hasAuthority('BookManage')")
    @PostMapping("/add")
    @ApiOperation(value = "添加书籍")
    public ResVo<Map<String,String>> addBook(
            @Validated(CreateBookGroup.class) @RequestBody BookCreateReq req) {
        Map<String,String> urls= bookService.saveBook(req);
        return ResVo.ok(urls);
    }



    @PreAuthorize("hasAuthority('BookManage')")
    @DeleteMapping("/remove")
    @ApiOperation(value = "下架书籍")
    public ResVo<String> removeBook(
            @RequestParam
            @NotNull(message="bookId不能为空")
            Long bookId) {
        bookService.removeBook(bookId);
        return ResVo.ok();
    }

    @PreAuthorize("hasAuthority('BookManage')")
    @PostMapping("/update")
    @ApiOperation(value = "更新书籍")
    public ResVo<Map<String,String>> updateBook(
            @Validated(UpdateBookGroup.class)
            @RequestBody
            BookCreateReq req) {
        Map<String,String> urls= bookService.updateBook(req);
        return ResVo.ok(urls);
    }
    @PreAuthorize("hasAuthority('BookManage')")
    @PostMapping("/image/notify")
    @ApiOperation(value = "保存书籍图片Url")
    public ResVo<String> saveImageUrl(
            @NotNull(message = "urlList不能为空")
            @RequestBody List<String> urlList)  {
        bookService.saveImageUrl(urlList);
        return ResVo.ok("ok");
    }
    @PostMapping("/page")
    @ApiOperation(value = "分页查询书籍")
    public ResVo<PageResult<BookDTO>> page(
            @RequestBody QueryBookReq req){

        return ResVo.ok(bookService.list(req));
    }
    @PreAuthorize("hasAuthority('BookManage')")
    @PostMapping("/query")
    @ApiOperation(value = "查询书籍")
    public ResVo<PageResult<BookDTO>> query(
            @RequestBody QueryBookReq req){
        return ResVo.ok(bookService.query(req));
    }

    @PostMapping("/update/status")
    @ApiOperation(value = "更新书籍状态")
    public ResVo<String> updateBookStatus(
            @ApiParam(value = "书籍id",required = true)
            @RequestParam
            @NotNull(message = "bookId不能为空")Long bookId,
            @ApiParam(value = "书籍状态",required = true)
            @RequestParam
            @NotNull(message = "bookStatus不能为空")String bookStatus){
        bookService.updateBookStatus(bookId,bookStatus);
        return ResVo.ok();
    }

    @PostMapping("/search")
    @ApiOperation(value = "搜索书籍")
    public ResVo<PageResult<BookBriefDTO>> search(@RequestBody QueryBookReq req){
        return ResVo.ok(bookService.search(req));

    }


}

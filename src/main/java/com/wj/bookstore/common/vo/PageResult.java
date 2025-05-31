package com.wj.bookstore.common.vo;

import lombok.Data;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-02-15-15:38
 **/
@Data
public class PageResult<T> {
    private long pageNum;
    private long pageSize;
    private long pages;
    private long total;
    private List<T> list;
}

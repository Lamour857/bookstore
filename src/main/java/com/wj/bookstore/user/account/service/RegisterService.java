package com.wj.bookstore.user.account.service;


import com.wj.bookstore.common.vo.req.UserReq;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-14-13:56
 **/

public interface RegisterService {

    void registerByPhone(UserReq userReq);
}

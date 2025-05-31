package com.wj.bookstore.user.account.service;

import com.wj.bookstore.common.vo.PageResult;
import com.wj.bookstore.user.account.entity.DTO.UserDTO;
import com.wj.bookstore.user.account.entity.req.ChangePasswordReq;
import com.wj.bookstore.user.account.entity.req.QueryUserReq;
import com.wj.bookstore.user.account.entity.req.UserReq;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-08-15:28
 **/
public interface UserService {
    UserDTO getUserInfo();

    void updateUserInfo(UserReq req);

    void sendVerifyCode(String phone);

    void changePassword(ChangePasswordReq req);

    void updateUserStatus(Long userId, Boolean isEnable);

    PageResult<UserDTO> queryUserList(QueryUserReq req);
}

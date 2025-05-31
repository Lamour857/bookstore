package com.wj.bookstore.user.account.service.impl;


import com.wj.bookstore.common.enums.StatusEnum;
import com.wj.bookstore.common.enums.VerifyCodeTypeEnum;
import com.wj.bookstore.common.utils.ExceptionUtil;
import com.wj.bookstore.common.vo.req.UserReq;
import com.wj.bookstore.user.account.repository.dao.UserDao;
import com.wj.bookstore.user.account.entity.DO.UserDO;
import com.wj.bookstore.user.account.service.RegisterService;
import com.wj.bookstore.user.authentication.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-14-13:58
 **/
@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    UserDao userDao;
    @Autowired
    CheckService checkService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void registerByPhone(UserReq userReq) {
        // 1. 检查验证码是否正确
        if(!checkService.checkVerifyCode(userReq.getPhone(), VerifyCodeTypeEnum.REGISTER.getType(), userReq.getVerifyCode())){
            throw ExceptionUtil.of(StatusEnum.VERIFY_CODE_NOT_EXIST);
        }
        // 2. 检查用户名是否重复
        UserDO user=userDao.getUserByUserName(userReq.getUsername());
        if (user != null) {
            throw ExceptionUtil.of(StatusEnum.USER_NAME_REPEAT);
        }
        user=new UserDO();
        user.setUsername(userReq.getUsername());
        // 对密码进行加密处理
        user.setPassword(passwordEncoder.encode(userReq.getPassword()));
        user.setPhone(userReq.getPhone());
        userDao.save(user);
    }

}

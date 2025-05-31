package com.wj.bookstore.user.account.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wj.bookstore.common.enums.GenderEnum;
import com.wj.bookstore.common.enums.StatusEnum;
import com.wj.bookstore.common.enums.VerifyCodeTypeEnum;
import com.wj.bookstore.common.enums.YesOrNoEnum;
import com.wj.bookstore.common.utils.ExceptionUtil;
import com.wj.bookstore.common.vo.PageResult;
import com.wj.bookstore.core.sms.SmsService;
import com.wj.bookstore.user.account.converter.UserConverter;
import com.wj.bookstore.user.account.entity.DTO.UserDTO;
import com.wj.bookstore.user.account.entity.DO.UserDO;
import com.wj.bookstore.user.account.entity.req.ChangePasswordReq;
import com.wj.bookstore.user.account.entity.req.QueryUserReq;
import com.wj.bookstore.user.account.entity.req.UserReq;
import com.wj.bookstore.user.account.repository.dao.UserDao;
import com.wj.bookstore.user.account.service.UserService;
import com.wj.bookstore.user.authentication.cache.TokenCache;
import com.wj.bookstore.user.authentication.service.CheckService;
import com.wj.bookstore.user.authentication.token.AuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-08-15:29
 **/
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserConverter userConverter;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SmsService smsService;
    @Autowired
    private CheckService checkService;
    @Autowired
    private TokenCache tokenCache;
    @Override
    public UserDTO getUserInfo() {
        AuthenticationToken authenticationToken= (AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDO user= authenticationToken.getUser();
        return userConverter.toUserDTO(user);
    }

    @Override
    public void updateUserInfo(UserReq req) {
        AuthenticationToken authenticationToken= (AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDO user= authenticationToken.getUser();
        if(req.getPassword()!=null){
            user.setPassword(passwordEncoder.encode(req.getPassword()));
        }
        if(req.getUsername()!=null&&!req.getUsername().equals(user.getUsername())){
            if(userDao.getUserByUserName(req.getUsername())!=null){
                throw ExceptionUtil.of(StatusEnum.RECORDS_EXISTS,"该用户名已存在"+req.getUsername());
            }
            user.setUsername(req.getUsername());
        }
        if(req.getPhone()!=null&&!req.getPhone().equals(user.getPhone())){
            if(userDao.getUserByPhone(req.getPhone())!=null){
                throw ExceptionUtil.of(StatusEnum.RECORDS_EXISTS,"该手机号已被注册"+req.getPhone());
            }
            user.setPhone(req.getPhone());
        }
        if(req.getGender()!=null&&GenderEnum.getCode(req.getGender())!=user.getGender()){
            user.setGender(GenderEnum.getCode(req.getGender()));
        }
        if(req.getLocationId()!=null&&!req.getLocationId().equals(user.getLocationId())){
            user.setLocationId(req.getLocationId());
        }
        if(req.getProfile()!=null&& !req.getProfile().equals(user.getProfile())){
            user.setProfile(req.getProfile());
        }
        if(req.getBirthday()!=null&&!req.getBirthday().equals(user.getBirthday())){
            user.setBirthday(req.getBirthday());
        }
        if(req.getPassword()!=null&&req.getRePassword()!=null&&req.getPassword().equals(req.getRePassword())){
            user.setPassword(passwordEncoder.encode(req.getPassword()));
        }
        userDao.updateById(user);
        tokenCache.cacheToken(authenticationToken.getToken(),user,86400000);
    }



    @Override
    public void sendVerifyCode(String phone) {
        AuthenticationToken authenticationToken= (AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDO user= authenticationToken.getUser();

        if(!user.getPhone().equals(phone)){
            throw ExceptionUtil.of(StatusEnum.UNEXPECT_ERROR,"手机号不匹配 {}",phone);
        }
        smsService.sendVerifyCode(phone, VerifyCodeTypeEnum.CHANGE_PASSWORD.getType());

    }

    @Override
    public void changePassword(ChangePasswordReq req) {
        AuthenticationToken authenticationToken= (AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDO user= authenticationToken.getUser();

        if(!checkService.checkVerifyCode(user.getPhone(), VerifyCodeTypeEnum.CHANGE_PASSWORD.getType(), req.getVerifyCode())){
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS,"验证码错误");
        }
        if(!req.getRePassword().equals(req.getPassword())){
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS,"两次密码不一致");
        }
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        userDao.updateById(user);
    }

    @Override
    public void updateUserStatus(Long userId, Boolean isEnable) {
        AuthenticationToken authenticationToken= (AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDO user= authenticationToken.getUser();

        if(user.getId().equals(userId)){
            throw ExceptionUtil.of(StatusEnum.UNEXPECT_ERROR,"不能禁用自己");
        }
        UserDO userDO=userDao.getById(userId);
        if(userDO==null){
            throw ExceptionUtil.of(StatusEnum.RECORDS_NOT_EXISTS,"用户不存在");
        }

        userDO.setEnabled(isEnable);
        userDao.updateById(userDO);
    }

    @Override
    public PageResult<UserDTO> queryUserList(QueryUserReq req) {
        PageResult<UserDTO> pageResult=new PageResult<>();
        // 从数据库中获取数据
        Page<UserDO> page=userDao.queryByPage(req);
        pageResult.setPageNum(page.getCurrent());
        pageResult.setPageSize(page.getSize());
        pageResult.setTotal(page.getTotal());
        List<UserDTO> userDTOList= new ArrayList<>();
        for (UserDO userDO : page.getRecords()) {
            userDTOList.add(userConverter.toUserDTO(userDO));
        }
        pageResult.setList( userDTOList);
        pageResult.setPages(page.getPages());
        return pageResult;
    }
}

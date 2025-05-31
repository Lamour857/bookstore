package com.wj.bookstore.user.authentication.service;


import com.wj.bookstore.user.account.repository.dao.UserDao;
import com.wj.bookstore.user.account.entity.DO.UserDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-19-20:34
 **/
@Slf4j
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private UserDao userDao;
    @Override
    public UserDO loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.getUserByUserName(username);

    }
    public UserDO loadUserByPhone(String phone) throws UsernameNotFoundException {
        return userDao.getUserByPhone(phone);
    }


}

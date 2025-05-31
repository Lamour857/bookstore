package com.wj.bookstore.user.account.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wj.bookstore.user.account.entity.DO.PermissionDO;
import com.wj.bookstore.user.account.entity.DO.PermissionViewDO;
import com.wj.bookstore.user.account.repository.mapper.PermissionMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-09-11:59
 **/
@Repository
public class PermissionDao extends ServiceImpl<PermissionMapper, PermissionDO> {

    public List<PermissionDO> list(Integer type) {
        LambdaQueryWrapper<PermissionDO> queryWrapper = Wrappers.lambdaQuery();
        if(type != null){
            queryWrapper.eq(PermissionDO::getType, type);
        }
        return getBaseMapper().selectList(queryWrapper);
    }

}

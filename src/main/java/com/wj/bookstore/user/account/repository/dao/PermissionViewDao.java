package com.wj.bookstore.user.account.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wj.bookstore.user.account.entity.DO.PermissionViewDO;
import com.wj.bookstore.user.account.repository.mapper.PermissionViewMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-15-13:53
 **/
@Repository
public class PermissionViewDao extends ServiceImpl<PermissionViewMapper, PermissionViewDO> {
    public List<PermissionViewDO> getViewsByPermissionId(Long id) {
        LambdaQueryWrapper<PermissionViewDO> queryWrapper = Wrappers.lambdaQuery();
        if(id!=null){
            queryWrapper.eq(PermissionViewDO::getPermissionId, id);
        }
        return this.baseMapper.selectList(queryWrapper);
    }
}

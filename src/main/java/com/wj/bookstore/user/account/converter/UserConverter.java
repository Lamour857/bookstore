package com.wj.bookstore.user.account.converter;

import com.wj.bookstore.common.enums.GenderEnum;

import com.wj.bookstore.common.utils.DateUtil;
import com.wj.bookstore.delivery.template.entity.DO.AreaDO;
import com.wj.bookstore.delivery.template.repository.dao.AreaDao;
import com.wj.bookstore.user.account.entity.DO.RoleDO;
import com.wj.bookstore.user.account.entity.DTO.UserDTO;
import com.wj.bookstore.user.account.entity.DO.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-08-15:33
 **/
@Component
public class UserConverter {
    @Autowired
    private AreaDao areaDao;
    public  UserDTO toUserDTO(UserDO userDO){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userDO.getId());
        userDTO.setUsername(userDO.getUsername());
        userDTO.setPhone(userDO.getPhone());
        userDTO.setGender(GenderEnum.fromCode(userDO.getGender()));
        userDTO.setLocationId(userDO.getLocationId());
        userDTO.setProfile(userDO.getProfile());
        userDTO.setGender(GenderEnum.fromCode(userDO.getGender()));
        userDTO.setBirthday(userDO.getBirthday());
        userDTO.setCreateTime(DateUtil.convert(userDO.getCreateTime()));
        userDTO.setUpdateTime(DateUtil.convert(userDO.getUpdateTime()));
        userDTO.setRoles(userDO.getRoles().getDescription());
        userDTO.setStatus(userDO.isEnabled()?"正常":"禁用");
        if(userDO.getLocationId()==null){
            userDTO.setProvince("");
            userDTO.setCity("");
            userDTO.setCounty("");
            return userDTO;
        }
        AreaDO address= areaDao.getById(userDO.getLocationId());
        Short level=address.getLevel();
        switch (level){
            case 1:
                // 省级
                userDTO.setProvince(address.getName());
                break;
            case 2:
                // 市级
                AreaDO city = areaDao.getById(address.getParent());
                userDTO.setProvince(address.getName());
                userDTO.setCity(city.getName());
                break;
            case 3:
                // 区级
                AreaDO parent = areaDao.getById(address.getParent());
                AreaDO province= areaDao.getById(parent.getParent());
                userDTO.setCity(parent.getName());
                userDTO.setProvince(province.getName());
                userDTO.setCounty(address.getName());
                break;
        }
        return userDTO;
    }

}

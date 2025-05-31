package com.wj.bookstore.user.account.entity.DTO;

import com.wj.bookstore.common.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-08-15:25
 **/
@Data
@EqualsAndHashCode(callSuper= true)
public class UserDTO extends BaseDTO {
    private String username;
    private String phone;
    private String gender;
    private String province;
    private String status;
    private String city;
    private String county;
    private Short locationId;
    private String profile;
    private Date birthday;
    private String roles;
}

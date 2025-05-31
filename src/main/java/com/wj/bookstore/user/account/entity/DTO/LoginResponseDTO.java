package com.wj.bookstore.user.account.entity.DTO;

import com.wj.bookstore.user.account.entity.DO.PermissionViewDO;
import com.wj.bookstore.user.merchant.entity.DTO.MerchantDTO;
import lombok.Data;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-15-13:58
 **/
@Data
public class LoginResponseDTO {
    private String token;
    private List<PermissionViewDTO> permissions;
    private UserDTO userInfo;
    private MerchantDTO merchantInfo;
}

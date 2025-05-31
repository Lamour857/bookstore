package com.wj.bookstore.user.merchant.entity.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-06-16:38
 **/
@Data
public class WithdrawalRequestDTO {
    private String id;
    private Long merchantId;
    private String name;
    private String merchantName;
    private String status;
    private String refuseReason;
    private String certNumber;
    private String certType;
    private String ownerName;
    private String accountIdentify;
    private BigDecimal amount;
    private String accountIdentifyType;
    private Long handleAdminId;
    private String handleTime;
    private String createTime;
    private String updateTime;
}

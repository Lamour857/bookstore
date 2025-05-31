package com.wj.bookstore.user.merchant.entity.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-12-19:25
 **/
@Data
@TableName("withdrawal_req")
public class WithdrawalReqDO {
    private String id;
    private String name;
    private Long merchantId;
    private String status;
    private String certNumber;
    private String certType;
    private String refuseReason;
    private String accountIdentify;
    private BigDecimal amount;
    private String accountIdentifyType;
    private Long handleAdminId;
    private Date handleTime;
    private Date createTime;
    private Date updateTime;
}

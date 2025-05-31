package com.wj.bookstore.common.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-15-13:21
 **/
@Data
@Document(indexName = "base_index")
public class BaseDO implements Serializable {
    @TableId(type= IdType.AUTO)
    @Id
    protected Long id;

    protected Date createTime;
    protected Date updateTime;
}

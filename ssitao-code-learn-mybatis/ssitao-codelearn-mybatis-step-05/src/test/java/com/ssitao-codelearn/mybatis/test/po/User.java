package com.ssitao.code.learn.mybatis.test.po;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class User {

    private Long userId;
    private String userName;        // 头像
    private LocalDateTime createTime;        // 创建时间
    private LocalDateTime updateTime;        // 更新时间

}

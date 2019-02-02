package com.awifi.springmvcrocketmq.consumer.entity;

import lombok.Data;

import java.util.Date;

@Data
public class UserInfo {
    private Long id;
    private String userId;
    private Long userPoint;
    private String account;
    private Long fundAmount;
    private Date crtTime;
    private Date updTime;
}

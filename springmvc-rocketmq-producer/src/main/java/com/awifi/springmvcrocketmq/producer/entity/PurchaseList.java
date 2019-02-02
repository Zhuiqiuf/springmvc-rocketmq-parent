package com.awifi.springmvcrocketmq.producer.entity;

import lombok.Data;

import java.util.Date;

@Data
public class PurchaseList {
    private String id;
    private String userId;
    private String orderNumber;
    private String productId;
    private Date crtTime;
    private Date updTime;
}

package com.awifi.springmvcrocketmq.consumer.entity;

import lombok.Data;

import java.util.Date;

@Data
public class StocksInfo {
    private Long id;
    private String address;
    private String productId;
    private Long stocksNumber;
    private Date crtTime;
    private Date updTime;
}

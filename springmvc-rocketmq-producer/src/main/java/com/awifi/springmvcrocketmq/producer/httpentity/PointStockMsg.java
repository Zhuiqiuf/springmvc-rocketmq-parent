package com.awifi.springmvcrocketmq.producer.httpentity;

import lombok.Data;

@Data
public class PointStockMsg {
    private String appId;//鉴权
    private String accessToken;//登录凭证
    private String productId;//产品编号
    private String userId;//用户编号
    private String stocksNumber;//购买数量
    private String purchaseNum;//订单编号
    private long userPoint;//积分
}

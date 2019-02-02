package com.awifi.springmvcrocketmq.consumer.service;


import com.awifi.springmvcrocketmq.consumer.entity.PurchaseList;

public interface PurchaseService {
    int addPurchaseList(PurchaseList purchaseList, String orderNumber);
}

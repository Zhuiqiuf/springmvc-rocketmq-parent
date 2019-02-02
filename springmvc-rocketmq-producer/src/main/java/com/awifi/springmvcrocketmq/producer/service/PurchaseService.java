package com.awifi.springmvcrocketmq.producer.service;


import com.awifi.springmvcrocketmq.producer.entity.PurchaseList;

public interface PurchaseService {
    int addPurchaseList(PurchaseList purchaseList, String orderNumber);
}

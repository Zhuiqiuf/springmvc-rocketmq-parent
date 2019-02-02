package com.awifi.springmvcrocketmq.consumer.service.impl;

import com.awifi.springmvcrocketmq.consumer.entity.PurchaseList;
import com.awifi.springmvcrocketmq.consumer.mapper.PurchaseListMapper;
import com.awifi.springmvcrocketmq.consumer.service.PurchaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Zhuiqiuf 订单新增
 */
@Service
@Slf4j
public class PurchaseServiceImpl implements PurchaseService {


    @Autowired
    private PurchaseListMapper purchaseListMapper;


    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public int addPurchaseList(PurchaseList purchaseList, String orderNumber){
        log.info("addPurchaseList start");
        //int num=purchaseListMapper.insert(purchaseList);
        while(true){
            throw new RuntimeException();
        }
        //return num;
    }
}

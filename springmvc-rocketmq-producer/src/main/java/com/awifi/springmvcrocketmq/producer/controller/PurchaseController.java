package com.awifi.springmvcrocketmq.producer.controller;

import com.alibaba.fastjson.JSON;
import com.awifi.springmvcrocketmq.configuration.TransactionMQProducerAutoConfiguration;
import com.awifi.springmvcrocketmq.producer.entity.PurchaseList;
import com.awifi.springmvcrocketmq.producer.httpentity.PointStockMsg;
import com.awifi.springmvcrocketmq.producer.listener.TransactionListenerImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @author Zhuiqiuf
 * rocketMQ test ，用于作为消息生产者进行mq消息发送事务消息。
 */

@RestController
@Slf4j
public class PurchaseController {

    @Autowired
    private TransactionMQProducerAutoConfiguration transactionMQProducerAutoConfiguration;
    @Autowired
    private TransactionListenerImpl transactionListenerImpl;


    @Value("${spring.rocketmq.producer.topic}")
    private String producerTopic;

    @RequestMapping(value = "/purchase", method = RequestMethod.POST)
    public String purchase(@RequestBody Map<String,Object> params) {

        PurchaseList purchaseList=new PurchaseList();
        purchaseList.setOrderNumber(UUID.randomUUID().toString());
        purchaseList.setCrtTime(new Date());
        purchaseList.setId(UUID.randomUUID().toString().replace("-",""));
        purchaseList.setProductId(params.get("productId").toString());
        purchaseList.setUserId(params.get("userId").toString());

        PointStockMsg pointStockMsg=new PointStockMsg();
        pointStockMsg.setProductId(params.get("productId").toString());
        pointStockMsg.setStocksNumber(params.get("purchaseNum").toString());
        pointStockMsg.setUserId(params.get("userId").toString());
        pointStockMsg.setPurchaseNum(params.get("purchaseNum").toString());
        pointStockMsg.setUserPoint((long)(10+Math.random()*100));
        pointStockMsg.setAppId("aaa");
        pointStockMsg.setAccessToken("111");

        transactionMQProducerAutoConfiguration.getTransactionMQProducer().setTransactionListener(transactionListenerImpl);
        SendResult sendResult = null;
        try {
            //构造消息
            Message msg = new Message(producerTopic,// topic
                    "TagA",// tag
                    "OrderID001",// key
                    (JSON.toJSONString(pointStockMsg)).getBytes());// body

            sendResult = transactionMQProducerAutoConfiguration.getTransactionMQProducer().sendMessageInTransaction(msg, purchaseList);
            log.info("msg send:",sendResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendResult.toString();
    }

}

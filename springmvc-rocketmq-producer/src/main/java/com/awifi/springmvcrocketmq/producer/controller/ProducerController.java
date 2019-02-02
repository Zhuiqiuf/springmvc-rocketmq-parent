package com.awifi.springmvcrocketmq.producer.controller;

import com.awifi.springmvcrocketmq.configuration.DefaultMQProducerAutoConfiguration;
import com.awifi.springmvcrocketmq.configuration.TransactionMQProducerAutoConfiguration;
import com.awifi.springmvcrocketmq.producer.entity.TopicEntity;
import com.awifi.springmvcrocketmq.producer.listener.TransactionListenerImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
public class ProducerController {

    @Autowired
    private DefaultMQProducerAutoConfiguration defaultMQProducerAutoConfiguration;

    @Autowired
    private TransactionMQProducerAutoConfiguration transactionMQProducerAutoConfiguration;
    @Autowired
    private TransactionListenerImpl transactionListenerImpl;

    @Autowired
    private TopicEntity TopicEntity;


    @RequestMapping(value = "/sendMsg", method = RequestMethod.GET)
    public void sendMsg() {
        /**
         * topic和tags可通过配置文件配置进行管理 keys与业务id进行关联管理，结合关系型数据库操作！
         */
        Message msg = new Message(TopicEntity.getTestTopic(),// topic
                "TagC",// tag
                "OrderID001",// key
                ("Hello zhuiqiuf").getBytes());// body
        msg.setDelayTimeLevel(3);
        //defaultProducer.send(messages);
        try {
            defaultMQProducerAutoConfiguration.getDefaultProducer().send(msg,new SendCallback(){//消息发送异步方式
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println(sendResult);
                    //TODO 发送成功处理
                }

                @Override
                public void onException(Throwable e) {
                    System.out.println(e);
                    //TODO 发送失败处理
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/sendTransactionMsg", method = RequestMethod.GET)
    public String sendTransactionMsg(HttpServletRequest req) {
        transactionMQProducerAutoConfiguration.getTransactionMQProducer().setTransactionListener(transactionListenerImpl);
        SendResult sendResult = null;
        try {
            //构造消息
            Message msg = new Message(TopicEntity.getTestTopic(),// topic
                    "TagA",// tag
                    "OrderID001",// key
                    (req.getParameter("msg")).getBytes());// body

            sendResult = transactionMQProducerAutoConfiguration.getTransactionMQProducer().sendMessageInTransaction(msg, null);
           log.info("msg send:",sendResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendResult.toString();
    }
}


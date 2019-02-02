package com.awifi.springmvcrocketmq.producer.listener;

import com.awifi.springmvcrocketmq.producer.entity.PurchaseList;
import com.awifi.springmvcrocketmq.producer.enums.LocalTransactionStates;
import com.awifi.springmvcrocketmq.producer.service.PurchaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自己实现TransactionListener接口，并实现executeLocalTransaction方法(执行本地事务的，
 * 一般就是操作DB相关内容)和checkLocalTransaction方法(用来提供给broker进行会查本地事务消息的，
 * 把本地事务执行的结果存储到redis或者DB中都可以，为会查做数据准备)。
 *
 */
@Slf4j
@Component
public class TransactionListenerImpl implements TransactionListener {
    private AtomicInteger transactionIndex = new AtomicInteger(0);

    //本地存储事务状态，用于rocketmq上的事务回查。
    private ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<>();

    @Autowired
    private PurchaseService purchaseService;

    /**
     * 只执行本地事务，并且记录事务状态
     * @param msg
     * @param arg
     * @return
     */
    @Override

    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        try{
            //DB操作 应该带上事务 service -> dao
            //如果数据操作失败  需要回滚    同时返回RocketMQ一个失败消息  意味着 消费者无法消费到这条失败的消息
            //如果成功 就要返回一个rocketMQ成功的消息，意味着消费者将读取到这条消息
            PurchaseList purchaseList= (PurchaseList) arg;
            purchaseService.addPurchaseList(purchaseList,purchaseList.getOrderNumber());
            log.info(new Date()+"===> 本地事务执行成功，发送确认消息");
            localTrans.put(msg.getTransactionId(),1);
        }catch (Exception e){
            log.info(new Date()+"===> 本地事务执行失败！！！");
            localTrans.put(msg.getTransactionId(),2);
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        return LocalTransactionState.COMMIT_MESSAGE;
    }

    /**
     * 回查事务状态
     * @param msg
     * @return
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        log.info("事务消息回查事务状态！");
        Integer status = localTrans.get(msg.getTransactionId());
        //改造成枚举来表示
        if (null != status) {
            return LocalTransactionStates.getDescByCode(status);
        }
        return LocalTransactionState.ROLLBACK_MESSAGE;//默认回滚
    }
}

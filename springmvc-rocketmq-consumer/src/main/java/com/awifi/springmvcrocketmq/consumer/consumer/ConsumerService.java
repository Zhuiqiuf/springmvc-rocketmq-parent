package com.awifi.springmvcrocketmq.consumer.consumer;


import com.alibaba.fastjson.JSON;
import com.awifi.springmvcrocketmq.consumer.entity.StocksInfo;
import com.awifi.springmvcrocketmq.consumer.entity.TopicTagEntity;
import com.awifi.springmvcrocketmq.consumer.entity.UserInfo;
import com.awifi.springmvcrocketmq.consumer.service.PointService;
import com.awifi.springmvcrocketmq.consumer.service.StocksInfoService;
import com.awifi.springmvcrocketmq.event.RocketmqEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Zhuiqiuf
 * 消息消费者，监听rocketmq消费者接受到消息后发布的事件，然后进行业务逻辑处理。
 */
@Service
@Slf4j
public class ConsumerService {
    /**加积分service类注入*/
    @Autowired
    private PointService pointService;

    /**减库存操作service类注入*/
    @Autowired
    private StocksInfoService stocksInfoService;


    /**
     * 监听rocketmq消息并行进行加积分操作
     * @param event
     */
    @EventListener(condition = "#event.topic==@topicTagAddPoint.testTopic && #event.tag==@topicTagAddPoint.testTag")
    public void addPoint(RocketmqEvent event){
        DefaultMQPushConsumer consumer = event.getConsumer();
        log.info("receive msg and addPoint start");
        try {
            String params=event.getMsg("utf-8");
            Optional<UserInfo> userInfo = Optional.of(JSON.parseObject(params, UserInfo.class));
            userInfo.ifPresent(pointService::addPoint);//userInfo 存在则利用方法引用调用pointService的addPoint方法
            //pointService.addPoint();
        } catch (Exception e) {
            log.error("message consumer error should back!");
            if (event.getMessageExt().getReconsumeTimes() < 3) {//重复消费3次
                try {
                    consumer.sendMessageBack(event.getMessageExt(), 2);
                } catch (Exception e1) {
                    //TODO 消息消费失败，进行日志记录
                }
            } else {
                //TODO 消息消费失败，进行日志记录
            }
        }

    }

    /**
     *监听rocketmq消息进行减库存操作
     * @param event
     */

    @EventListener(condition = "#event.topic==@topicTagReduceStock.testTopic && #event.tag==@topicTagReduceStock.testTag")
    public void reduceStock(RocketmqEvent event){
        DefaultMQPushConsumer consumer = event.getConsumer();
        try {
            log.info("receive msg and reduceStock start");
            String params=event.getMsg("utf-8");
            Optional<StocksInfo> stocksInfo = Optional.of(JSON.parseObject(params, StocksInfo.class));
            stocksInfo.ifPresent(stocksInfoService::reduceStock);//stocksInfo 存在则利用方法引用调用stocksInfoService的reduceStock方法
            //stocksInfoService.reduceStock(stocksInfo);
        } catch (Exception e) {
            log.error("message consumer error should back!");
            if (event.getMessageExt().getReconsumeTimes() < 3) {//重复消费3次
                try {
                    consumer.sendMessageBack(event.getMessageExt(), 2);
                } catch (Exception e1) {
                    //TODO 消息消费失败，进行日志记录
                }
            } else {
                //TODO 消息消费失败，进行日志记录
            }
        }
    }

}


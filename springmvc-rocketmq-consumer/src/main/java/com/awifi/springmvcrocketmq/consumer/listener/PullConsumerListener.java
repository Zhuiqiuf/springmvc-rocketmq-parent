package com.awifi.springmvcrocketmq.consumer.listener;

import com.awifi.springmvcrocketmq.consumer.consumer.PullConsumerService;
import com.awifi.springmvcrocketmq.consumer.event.PullConsumerEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Zhuiqiuf
 * 早期的事件监听器，监听事件获取数据源进行消息处理
 */
/*@Slf4j
@Component
public class PullConsumerListener implements ApplicationListener<PullConsumerEvent> {

    @Autowired
    PullConsumerService pullConsumerService;


    @Async
    @Override
    public void onApplicationEvent(PullConsumerEvent pullConsumerEvent) {
        log.info("监听到消息事件pull并进行逻辑处理");
        *//*pullConsumerService.pullConsumerFetchMsg(pullConsumerEvent);*//*
    }
}*/

package com.awifi.springmvcrocketmq.consumer.listener;


import com.awifi.springmvcrocketmq.consumer.event.PullConsumerEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 初始化发布rocketmq消费者拉取消息
 * @author Zhuiqiuf
 */
@Component
@Slf4j
public class InitPublishEvent implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ApplicationEventPublisher publisher;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
            String bizCode="001";
            String json="{}";
            this.publisher.publishEvent(new PullConsumerEvent(json,bizCode));
            log.info("publish pullMsgConsumer exec ok!");
        }
    }

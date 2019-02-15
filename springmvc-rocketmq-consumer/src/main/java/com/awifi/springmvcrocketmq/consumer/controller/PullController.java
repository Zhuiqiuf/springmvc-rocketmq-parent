package com.awifi.springmvcrocketmq.consumer.controller;

import com.awifi.springmvcrocketmq.consumer.consumer.PullConsumerService;
import com.awifi.springmvcrocketmq.consumer.event.PullConsumerAnyEvent;
import com.awifi.springmvcrocketmq.consumer.event.PullConsumerEvent;
import com.awifi.springmvcrocketmq.consumer.utils.SpringContextPublisherUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zhuiqiuf
 * 消费者pull模式，主动拉取消息
 */

@RestController
@Slf4j
public class PullController {

    @Autowired
    private ApplicationEventPublisher publisher;

    /**
     * 消息事件通知拉取消息 code码001
     * 早期消息事件通知
     * @param bizCode
     * @param json
     * @return
     */
    @RequestMapping(value = "/pullConsumer", method = RequestMethod.GET)
    public String pullConsumer(@RequestParam("bizCode") String bizCode,@RequestParam(("json")) String json) {
        log.info("bizCode here "+bizCode+" and json here "+json);
        this.publisher.publishEvent(new PullConsumerEvent(json,bizCode));
        return "ok";
    }

    /**
     * 消息事件通知拉取消息 code码002
     * spring 4.2之后的可用任意类作为消息事件类
     * @param bizCode
     * @param json
     * @return
     */
    @RequestMapping(value = "/pullConsumerAny", method = RequestMethod.GET)
    public String pullConsumerAny(@RequestParam("bizCode") String bizCode,@RequestParam(("json")) String json) {
        log.info("bizCode here "+bizCode+" and json here "+json);
        this.publisher.publishEvent(new PullConsumerAnyEvent(json,bizCode));
        return "ok";
    }

    /**
     * 消息事件通知消息拉取 code码003
     * 异步方式
     * @param bizCode
     * @param json
     * @return
     */
    @RequestMapping(value = "/pullConsumerAsync", method = RequestMethod.GET)
    public String pullConsumerAsync(@RequestParam("bizCode") String bizCode,@RequestParam(("json")) String json) {
        log.info("bizCode here "+bizCode+" and json here "+json);
        SpringContextPublisherUtil.publishEvent(new PullConsumerAnyEvent(json,bizCode));
        log.info("publish Async end");
        return "ok";
    }
}

package com.awifi.springmvcrocketmq.producer.entity;

import lombok.Data;

/**
 * 自定义的用于灵活配置rocketmq中topic以及tag的配置类
 * @author Zhuiqiuf
 */
@Data
public class TopicEntity {
    private  String testTopic;
    private  String testTag;
}

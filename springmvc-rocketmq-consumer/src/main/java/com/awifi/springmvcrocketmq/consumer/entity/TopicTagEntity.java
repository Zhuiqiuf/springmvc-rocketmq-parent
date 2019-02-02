package com.awifi.springmvcrocketmq.consumer.entity;

import lombok.Data;

/**
 * 管理rocketmq topic和tag的实体类
 * @author Zhuiqiuf
 */
@Data
public class TopicTagEntity {
    private  String testTopic;
    private  String testTag;
}

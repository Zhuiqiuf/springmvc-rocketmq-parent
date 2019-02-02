package com.awifi.springmvcrocketmq.consumer.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.context.ApplicationEvent;

import java.io.UnsupportedEncodingException;

/**
 * @author tangzy
 * 管理消费者pull消费模式各个业务消费场景分发事件
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class PullConsumerEvent extends ApplicationEvent {
    private static final long serialVersionUID = -1468405250074039867L;

    private String bizCode;

    public PullConsumerEvent(String json,String bizCode) {
        super(json);
        this.bizCode = bizCode;
    }

}

package com.awifi.springmvcrocketmq.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.context.ApplicationEvent;

import java.io.UnsupportedEncodingException;

/**
 * @author tangzy
 * 用于消费者push模式消费到消息后发布事件，让具体业务进行一步处理，达到解耦的目的！
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class RocketmqEvent extends ApplicationEvent {
    private static final long serialVersionUID = -4468405250074063206L;

    private DefaultMQPushConsumer consumer;
    private MessageExt messageExt;
    private String topic;
    private String tag;
    private byte[] body;

    public RocketmqEvent(MessageExt msg, DefaultMQPushConsumer consumer) {
        super(msg);
        this.topic = msg.getTopic();
        this.tag = msg.getTags();
        this.body = msg.getBody();
        this.consumer = consumer;
        this.messageExt = msg;
    }

    public String getMsg() {
        try {
            return new String(this.body,"utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public String getMsg(String code) {
        try {
            return new String(this.body,code);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

}

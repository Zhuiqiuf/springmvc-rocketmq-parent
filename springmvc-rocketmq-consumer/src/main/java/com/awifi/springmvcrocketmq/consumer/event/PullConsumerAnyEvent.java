package com.awifi.springmvcrocketmq.consumer.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.ApplicationEvent;

/**
 * @author tangzy
 * 管理消费者pull消费模式各个业务消费场景分发事件
 * spring4.2之后发布器可以发布任意自定义Event，而不是
 * 仅仅限定于继承ApplicationEvent的事件类
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class PullConsumerAnyEvent {
    private static final long serialVersionUID = -1468405250074039867L;

    private String bizCode;
    private String json;

    public PullConsumerAnyEvent(String json, String bizCode) {
        this.json = json;
        this.bizCode = bizCode;
    }
}

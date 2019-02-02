package com.awifi.springmvcrocketmq.configuration;

import com.awifi.springmvcrocketmq.conditional.ConsumerConditional;
import lombok.Data;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.Set;


/**
 * @author Zhuiqiuf
 * RocketMQ消息消费者 pull模式初始化自动装配
 */
@Component
@Data
public class DefaultMQPullConsumerAutoConfiguration {


    private DefaultMQPullConsumer consumer;

    /**地址列表，多个NameServer地址用分号隔开*/
    private String namesrvAddr;

    /**客户端实例名称，客户端创建的多个Producer,Consumer实际是公用一个内部实例（这个实例包含网络连接，线程资源等）*/
    private String instanceName;
    /**
     * 消费者组名
     */
    private String consumerGroupName;
    /**
     * 消息模型，支持以下两种：集群消费，广播消费
     */
    private String messageModel;

    /**长轮询，Consumer拉消息请求在Broker挂起最长时间，单位毫秒*/
    private long brokerSuspendMaxTimeMillis=20000;
    /**非长轮询，拉消息超时时间，单位毫秒*/
    private long consumerPullTimeoutMillis=10000;
    /**长轮询，Consumer拉消息请求Broker挂起超过指定时间，客户端认为超时，单位毫秒*/
    private long consumerTimeoutMillisWhenSuspend=30000;
    /**注册的topic集合*/
    private Set<String> registerTopics;

    /**
     * 初始化rocketmq 需要时拉取的消费模式的消费者
     */
    public void init() throws MQClientException {
        /**
         * 一个应用创建一个Consumer，由应用来维护此对象，可以设置为全局对象或者单例<br>
         * 注意：ConsumerGroupName需要由应用来保证唯一
         */
        consumer=new DefaultMQPullConsumer(consumerGroupName);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setInstanceName(instanceName);
        consumer.setBrokerSuspendMaxTimeMillis(brokerSuspendMaxTimeMillis);
        consumer.setConsumerPullTimeoutMillis(consumerPullTimeoutMillis);
        consumer.setConsumerTimeoutMillisWhenSuspend(consumerTimeoutMillisWhenSuspend);
        /**
         * 设置消费模型，集群还是广播，默认为集群
         */
        if("BROADCASTING".equals(messageModel)){
            consumer.setMessageModel(MessageModel.BROADCASTING);
        }
        //注册的topic集合
        consumer.setRegisterTopics(registerTopics);
        consumer.start();
    }

    /**
     * Spring bean destroy-method
     */
    public void destroy() {
        consumer.shutdown();
    }
}



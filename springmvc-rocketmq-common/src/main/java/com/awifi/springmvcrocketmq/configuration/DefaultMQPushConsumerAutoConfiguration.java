package com.awifi.springmvcrocketmq.configuration;

import com.awifi.springmvcrocketmq.conditional.ConsumerConditional;
import com.awifi.springmvcrocketmq.event.RocketmqEvent;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Zhuiqiuf
 * RocketMQ消息消费者 pull模式初始化自动装配
 */
@Component
@Slf4j
@Data
public class DefaultMQPushConsumerAutoConfiguration {


    private DefaultMQPushConsumer consumer;

    /**地址列表，多个NameServer地址用分号隔开*/
    private String namesrvAddr;

    /**客户端实例名称，客户端创建的多个Producer,Consumer实际是公用一个内部实例（这个实例包含网络连接，线程资源等）*/
    private String instanceName;
    /**
     * 消费者组名
     */
    private String consumerGroupName;

    /**订阅的（topic:tag) list*/
    private List<String> subscribe;
    /**
     * 消息模型，支持以下两种：集群消费，广播消费
     */
    private String messageModel;
    /**消费线程池数量*/
    private Integer consumeThreadMin=20;
    /**消费线程池数量*/
    private Integer consumeThreadMax=64;
    /**批量消费，一次消费多少条消息*/
    private Integer consumeMessageBatchMaxSize=1;

    @Autowired
    private ApplicationEventPublisher publisher;


    
    /**
     * 初始化rocketmq主动拉取模式的消息监听方式的消费者
     */
    public void init() throws MQClientException{
        /**
         * 一个应用创建一个Consumer，由应用来维护此对象，可以设置为全局对象或者单例<br>
         * 注意：ConsumerGroupName需要由应用来保证唯一
         */
        consumer = new DefaultMQPushConsumer(consumerGroupName);
        /**
         * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
         * 如果非第一次启动，那么按照上次消费的位置继续消费
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setInstanceName(instanceName);
        consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);//设置批量消费，以提升消费吞吐量，默认是1

        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);
        /**
         * 设置消费模型，集群还是广播，默认为集群
         */
        if("BROADCASTING".equals(messageModel)){
            consumer.setMessageModel(MessageModel.BROADCASTING);
        }

        /**
         * 订阅指定topic下tags  topic:tag拼接
         */
        List<String> subscribeList = subscribe;
        for (String sunscribe : subscribeList) {
            consumer.subscribe(sunscribe.split(":")[0], sunscribe.split(":")[1]);
        }

        /**
         * ConsumeConcurrentlyContext并发消费模式
         * MessageListenerOrderly有序消费模式
         * 并发模式处理效率更佳，消费失败的话有序消费模式会立即重新消费  并发模式会有延迟！
         */
        consumer.registerMessageListener((List<MessageExt> msgs, ConsumeConcurrentlyContext context) -> {
            MessageExt msg = msgs.get(0);
            try {
                //默认msgs里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息
                log.info(Thread.currentThread().getName() + "Receive New Messages: " + msgs.size());
                //发布消息到达的事件，以便分发到每个tag的监听方法
                this.publisher.publishEvent(new RocketmqEvent(msg,consumer));
                log.info("消息到达事件已经发布成功！");
            } catch (Exception e) {
               log.error("msg reconsumeTimes_"+msg.getReconsumeTimes()+e);
                log.error("msg reconsumeTimes_"+msg.getMsgId()+e);
                if(msg.getReconsumeTimes()<3){//重复消费3次
                    //TODO 进行日志记录
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                } else {
                    //TODO 消息消费失败，进行日志记录
                }
            }
            //如果没有return success，consumer会重复消费此信息，直到success。
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        new Thread(() -> {
            try {
                Thread.sleep(5000);//延迟5秒再启动，主要是等待spring事件监听相关程序初始化完成，否则，会出现对RocketMQ的消息进行消费后立即发布消息到达的事件，然而此事件的监听程序还未初始化，从而造成消息的丢失
                /**
                 * Consumer对象在使用之前必须要调用start初始化，初始化一次即可<br>
                 */
                try {
                    consumer.start();
                } catch (Exception e) {
                    log.info("RocketMq pushConsumer Start failure!!!.");
                    e.printStackTrace();
                }
                log.info("RocketMq pushConsumer Started.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }


    /**
     * Spring bean destroy-method
     */
    public void destroy() {
        consumer.shutdown();
    }
}



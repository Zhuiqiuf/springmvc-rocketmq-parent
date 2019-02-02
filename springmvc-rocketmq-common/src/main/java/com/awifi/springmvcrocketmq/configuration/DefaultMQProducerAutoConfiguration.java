package com.awifi.springmvcrocketmq.configuration;

import lombok.Data;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.stereotype.Component;


/**
 * @author Zhuiqiuf
 * RocketMQ普通消息生产者初始化自动装配
 */
@Data
@Component
public class DefaultMQProducerAutoConfiguration {

    private DefaultMQProducer defaultProducer;

    /**地址列表，多个NameServer地址用分号隔开*/
    private String namesrvAddr;

    /**客户端实例名称，客户端创建的多个Producer,Consumer实际是公用一个内部实例（这个实例包含网络连接，线程资源等）*/
    private String instanceName;

    /**
     * Producer组名，多个Producer如果属于一个应用，发送同样的消息，则应该将他们归为同一组
     */
    private String producerGroup="DEFAULT_PRODUCER";
    /**
     * Broker回查Producer事务状态时，线程池大小
     */
    private Integer checkThreadPoolMinSize;
    /**
     * Broker回查Producer事务状态时，线程池大小
     */
    private Integer checkThreadPoolMaxSize = 1;
    /**
     * Broker回查Producer事务状态时，Produceer本地缓冲请求队列大小
     */
    private Integer checkRequestHoldMax = 2000;

    /**
     * 消息最大大小，默认4M
     */
    private Integer maxMessageSize=1024*1024*4;
    /**
     * 消息发送超时时间，默认3秒
     */
    private Integer sendMsgTimeout=3000;
    /**
     * 消息发送失败重试次数，默认2次
     */
    private Integer retryTimesWhenSendFailed=2;

    /**
     * vip通道
     */
    private boolean vipChannelEnabled=false;


    /**
     * 初始化向rocketmq发送普通消息的生产者
     */
    //@Bean
    //@Conditional(ProducerConditional.class)
    public void init() throws MQClientException {
        /**
         * 一个应用创建一个Producer，由应用来维护此对象，可以设置为全局对象或者单例<br>
         * 注意：ProducerGroupName需要由应用来保证唯一<br>
         * ProducerGroup这个概念发送普通的消息时，作用不大，但是发送分布式事务消息时，比较关键，
         * 因为服务器会回查这个Group下的任意一个Producer
         */
        defaultProducer = new DefaultMQProducer(producerGroup);
        defaultProducer.setNamesrvAddr(namesrvAddr);
        defaultProducer.setInstanceName(instanceName);
        defaultProducer.setVipChannelEnabled(vipChannelEnabled);
        defaultProducer.setMaxMessageSize(maxMessageSize);
        defaultProducer.setSendMsgTimeout(sendMsgTimeout);
        //如果发送消息失败，设置重试次数，默认为2次
        defaultProducer.setRetryTimesWhenSendFailed(retryTimesWhenSendFailed);

        /**
         * Producer对象在使用之前必须要调用start初始化，初始化一次即可<br>
         * 注意：切记不可以在每次发送消息时，都调用start方法
         */
        defaultProducer.start();
    }
    /**
     * Spring bean destroy-method
     */
    public void destroy() {
        defaultProducer.shutdown();
    }
}

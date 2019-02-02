package com.awifi.springmvcrocketmq.configuration;

import com.awifi.springmvcrocketmq.conditional.TransactionProducerConditional;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;


/**
 * @author Zhuiqiuf
 * RocketMQ事务消息生产者初始化自动装配
 */
@Slf4j
@Component
@Data
public class TransactionMQProducerAutoConfiguration {

    private TransactionMQProducer transactionMQProducer;

    /**地址列表，多个NameServer地址用分号隔开*/
    private String namesrvAddr;

    /**客户端实例名称，客户端创建的多个Producer,Consumer实际是公用一个内部实例（这个实例包含网络连接，线程资源等）*/
    private String tranInstanceName;

    /**
     * Producer组名，多个Producer如果属于一个应用，发送同样的消息，则应该将他们归为同一组
     */
    private String producerGroup="DEFAULT_PRODUCER";
    /**
     * 消息发送线程核心线程数
     */
    private Integer checkThreadPoolMinSize=2;
    /**
     * 消息发送线程最大线程数
     */
    private Integer checkThreadPoolMaxSize = 5;
    /**
     * 消息发送线程最大线程数阻塞队列最大数
     */
    private Integer checkRequestHoldMax = 2000;

    /**
     * 消息发送线程超时时间
     */
    private Integer keepAliveTime=100;

    /**
     * 消息最大大小，默认4M
     */
    private Integer maxMessageSize=1024*1024*4 ;
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
     * 初始化向rocketmq发送事务消息的生产者
     */
    public void init() throws MQClientException{
        /**
         * 一个应用创建一个Producer，由应用来维护此对象，可以设置为全局对象或者单例<br>
         * 注意：ProducerGroupName需要由应用来保证唯一<br>
         * ProducerGroup这个概念发送普通的消息时，作用不大，但是发送分布式事务消息时，比较关键，
         * 因为服务器会回查这个Group下的任意一个Producer
         */
        transactionMQProducer = new TransactionMQProducer(producerGroup);
        transactionMQProducer.setNamesrvAddr(namesrvAddr);
        transactionMQProducer.setInstanceName(tranInstanceName);

        /**重发线程*/
        ExecutorService executorService = new ThreadPoolExecutor(checkThreadPoolMinSize, checkThreadPoolMaxSize, keepAliveTime, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(checkRequestHoldMax), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("client-transaction-msg-check-thread");
                return thread;
            }
        });

        transactionMQProducer.setExecutorService(executorService);
        transactionMQProducer.setVipChannelEnabled(vipChannelEnabled);

        /**
         * Producer对象在使用之前必须要调用start初始化，初始化一次即可<br>
         * 注意：切记不可以在每次发送消息时，都调用start方法
         */
        transactionMQProducer.start();

        log.info("RocketMq TransactionMQProducer Started.");
    }
    /**
     * Spring bean destroy-method
     */
    public void destroy() {
        transactionMQProducer.shutdown();
    }
}



package com.awifi.springmvcrocketmq.consumer.consumer;


import com.awifi.springmvcrocketmq.configuration.DefaultMQPullConsumerAutoConfiguration;
import com.awifi.springmvcrocketmq.consumer.configuration.BusinessCaseConfiguration;
import com.awifi.springmvcrocketmq.consumer.event.PullConsumerAnyEvent;
import com.awifi.springmvcrocketmq.consumer.event.PullConsumerEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * RocketMq pull消费类型测试代码
 * 需设置消费类型为是否为pull，不设置为pull则默认为push
 * spring.rocketmq.consumer.pullConsumeEnable=true
 */
@Component
@Slf4j
public class PullConsumerService {


    @Autowired
    private DefaultMQPullConsumerAutoConfiguration pullConsumer;

    @Autowired
    BusinessCaseConfiguration businessCase;

    @Autowired
    @Qualifier("noBalanceRestTemplate")
    private RestTemplate noBalanceRestTemplate;

    //保存上一次消费的消息位置
    private static final Map offsetTable = new HashMap();

    @EventListener(condition = "#event.bizCode==@businessCaseConfiguration.getCodes('testPullCode')")
    public void pullConsumerFetchMsg(PullConsumerAnyEvent event){
        Set<MessageQueue> mqs = null;
        try {
            //拉取PullConsumerTopicTest topic下的所有消息队列
            mqs = pullConsumer.getConsumer().fetchSubscribeMessageQueues(businessCase.getTopics("testPullTopic"));
        } catch (MQClientException e) {
            log.error("connet MQClient exception!!"+e);
            return;
        }
        //遍历消息队列
        for (MessageQueue mq : mqs) {
            System.err.println("Consume from the queue: " + mq);
            SINGLE_MQ:
            while (true) {
                try {
                    //设置上次消费消息下标
                    long currentOffset=pullConsumer.getConsumer().fetchConsumeOffset(mq,false);
                    PullResult pullResult = pullConsumer.getConsumer().pullBlockIfNotFound(mq, null, currentOffset, 32);
                    System.err.println("当前偏移量 "+currentOffset+" 下一个偏移量 "+pullResult.getNextBeginOffset());
                    //更新偏移量
                    switch (pullResult.getPullStatus()) {
                        //根据结果状态，如果找到消息，批量消费消息
                        case FOUND:
                            List<MessageExt> messageExtList = pullResult.getMsgFoundList();
                            for (MessageExt m : messageExtList) {
                                String url="http://192.168.212.73:8901/oauth/checkToken";//鉴权
                                log.info("Remote invocation of third-party services");
                                String body=new String(m.getBody(),"utf-8");
                                HttpEntity<String> httpEntity = new HttpEntity<>(body);
                                ResponseEntity<String> entity = noBalanceRestTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
                                log.info("receive msg"+entity.toString());
                            }
                            break;
                        case NO_MATCHED_MSG:
                            break;
                        case NO_NEW_MSG:
                            break SINGLE_MQ;
                        case OFFSET_ILLEGAL:
                            break;
                        default:
                            break;
                    }
                    pullConsumer.getConsumer().updateConsumeOffset(mq,pullResult.getNextBeginOffset());
                    log.info("[PULL]更新当前队列：{}，消费的消息偏移量：{}" ,mq.getQueueId(), pullResult.getNextBeginOffset());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }


    @EventListener(condition = "#event.bizCode==@businessCaseConfiguration.getCodes('testPullCode2')")
    public void pullConsumerTest(PullConsumerAnyEvent event){
        log.info("test the application Event");
    }

    @Async
    @EventListener(condition = "#event.bizCode==@businessCaseConfiguration.getCodes('testPullCode3')")
    public void pullConsumerAsync(PullConsumerAnyEvent event){
        log.info("test the application Event Async");
    }
}


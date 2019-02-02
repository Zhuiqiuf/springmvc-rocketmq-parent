package com;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

/**
 * 消息的发送和接收都要有对应的Topic，<br>
 * 所以首先在服务器上创建 topic：bin/mqadmin updateTopic -c rocketmq-cluster -n '192.168.212.75:9876;192.168.212.76:9876' -t TopicTest
 *
 * @author fengxuechao
 * @date 2019/1/16
 **/
public class ProducerApp {

    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException, MQBrokerException {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer("rmq-group");
        // Specify name server addresses.
        producer.setNamesrvAddr("192.168.212.75:9876;192.168.212.76:9876");
//        producer.setVipChannelEnabled(false);
        producer.setInstanceName("rocketmq-test");
        //Launch the instance.
        producer.start();
        for (int i = 0; i < 10; i++) {
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message(
                    "testTopic",
                    "TagA",
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET)
            );
            //Call send message to deliver message to one of brokers.
            SendResult sendResult = producer.send(msg);
            System.out.printf("%s%n", sendResult);
        }
        //Shut down once the producer instance is not longer in use.
        producer.shutdown();
    }
}

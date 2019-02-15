package com.awifi.springmvcrocketmq.producer.configuration;

import com.awifi.springmvcrocketmq.producer.entity.TopicEntity;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * 管理消息生产者业务上的各种topics以及tags
 * @author Zhuiqiuf
 */
@Configuration
@PropertySource(value = "classpath:topics.properties")
@Data
public class TopicConfiguration {

    @Autowired
    Environment environment;

    @Bean
    public TopicEntity topicEntity(){
        TopicEntity topicEntity= new TopicEntity();
        topicEntity.setTestTopic(environment.getProperty("consumer.addPoint.topic"));
        topicEntity.setTestTag(environment.getProperty("consumer.addPoint.tag"));
        return topicEntity;
    }
}

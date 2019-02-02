package com.awifi.springmvcrocketmq.producer.config;

import com.awifi.springmvcrocketmq.producer.entity.TopicEntity;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 管理业务上的各种topics
 * @author Zhuiqiuf
 */
@Configuration
@PropertySource(value = "classpath:rocketmq.properties")
@Data
public class TopicConfiguration {

    @Value("${spring.rocketmq.producer.topic}")
    private String testTopic;

    @Bean
    public TopicEntity topicEntity(){
        TopicEntity topicEntity= new TopicEntity();
        topicEntity.setTestTopic(testTopic);
        return topicEntity;
    }
}

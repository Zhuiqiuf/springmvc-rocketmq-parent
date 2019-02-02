package com.awifi.springmvcrocketmq.consumer.configuration;

import com.awifi.springmvcrocketmq.consumer.entity.TopicTagEntity;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * 管理业务上的各种topics以及tags
 * 专门定义topictags.properties来管理配置，然后通过此类初始化加载
 * @author Zhuiqiuf
 */
@Configuration
@PropertySource(value = "classpath:topics.properties")
@Data
public class TopicTagsConfiguration {
    @Autowired
    Environment environment;

    /*@Value("${spring.rocketmq.consumer.topic}")
    private String testTopic;
    @Value("${spring.rocketmq.consumer.tag}")
    private String testTag;*/

    /**
     * 为了便于扩展，每一个业务场景对应一个bean
     * 这里是加积分业务场景的topic以及tag配置
     * @return
     */
    @Bean
    public TopicTagEntity topicTagAddPoint(){
        TopicTagEntity entity= new TopicTagEntity();
        entity.setTestTopic(environment.getProperty("consumer.addPoint.topic"));
        entity.setTestTag(environment.getProperty("consumer.addPoint.tag"));
        return entity;
    }

    /**
     * 为了便于扩展，每一个业务场景对应一个bean
     * 这里是减库存业务场景的topic以及tag配置
     * @return
     */
    @Bean
    public TopicTagEntity topicTagReduceStock(){
        TopicTagEntity entity= new TopicTagEntity();
        entity.setTestTopic(environment.getProperty("consumer.reduceStock.topic"));
        entity.setTestTag(environment.getProperty("consumer.reduceStock.tag"));
        return entity;
    }
}

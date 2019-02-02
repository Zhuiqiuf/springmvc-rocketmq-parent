package com.awifi.springmvcrocketmq.consumer.configuration;

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
@PropertySource(value = "classpath:businesscase.properties")
@Data
public class BusinessCaseConfiguration {
    @Autowired
    Environment environment;

    private  String code;

    private  String topic;


    public String getCodes(String keyCode){
        return environment.getProperty(keyCode);
    }

    public String getTopics(String keyTopic){
        return environment.getProperty(keyTopic);
    }

}

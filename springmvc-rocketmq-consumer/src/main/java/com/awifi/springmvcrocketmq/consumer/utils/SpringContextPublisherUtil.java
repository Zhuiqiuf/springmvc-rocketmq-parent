package com.awifi.springmvcrocketmq.consumer.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

/**
 * 自定义的用于发布事件的发布者
 * @author Zhuiqiuf
 * 需要实现ApplicationContextAware接口
 * 重写setApplicationContext，然后通过传递
 * applicationContext参数初始化成员变量applicationContext
 * 最后利用上下文去发布事件。
 */
@EnableAsync
@Component
public class SpringContextPublisherUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext; // Spring应用上下文环境

    /*
     *
     * 实现了ApplicationContextAware 接口，必须实现该方法；
     *
     * 通过传递applicationContext参数初始化成员变量applicationContext
     *
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextPublisherUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 早期的发布需要继承ApplicationEvent的事件
     * @param event
     */
    public static void publishEvent(ApplicationEvent event){
        applicationContext.publishEvent(event);
    }

    /**
     * spring4.2之后可以发布任意object
     * @param event
     */
    public static void publishEvent(Object event){
        applicationContext.publishEvent(event);
    }
}


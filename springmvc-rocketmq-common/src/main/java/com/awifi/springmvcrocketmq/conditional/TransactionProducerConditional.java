package com.awifi.springmvcrocketmq.conditional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class TransactionProducerConditional implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {

        Environment environment = conditionContext.getEnvironment();

        String property = environment.getProperty("spring.rocketmq.producer.tranInstanceName");
        return StringUtils.isNotBlank(property);
    }
}

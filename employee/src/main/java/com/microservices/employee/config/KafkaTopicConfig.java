package com.microservices.employee.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.topic-employee}")
    private String employeeTopic;
    @Bean
    public NewTopic employeeTopic(){
        return new NewTopic(employeeTopic, 3, (short) 1);
    }
}

package com.microservices.address.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageConsumer {

    Logger log = LoggerFactory.getLogger(KafkaMessageConsumer.class);

    @KafkaListener(
            topics = "${spring.kafka.topic-employee}",
            groupId = "${spring.kafka.consumer.group-id}",
            concurrency = "10"   // optional, works with batch too
    )
    public void employeeConsumer1(String message) throws InterruptedException {
        log.info("consumer1 message : {} : {}", message, Thread.currentThread().getName());
        Thread.sleep(300);
    }
}

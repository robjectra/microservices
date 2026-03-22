package com.microservices.employee.service;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class KafkaMessageProducer {

    private KafkaTemplate<String, Object> template;

    public  void sendMessage(String topic, Object message){
        CompletableFuture<SendResult<String, Object>> future = template.send(topic, message);
        future.whenComplete((result, ex)-> {
            if(ex ==null){
                System.out.println("Message send successfully");
            }else{
                System.out.println("Message not send");
            }
        });
    }

}

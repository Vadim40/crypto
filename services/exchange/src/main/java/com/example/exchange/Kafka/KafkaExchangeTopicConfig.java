package com.example.exchange.Kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

public class KafkaExchangeTopicConfig {
    @Bean
    public NewTopic exchangeTopic(){
        return TopicBuilder
                .name("exchange-topic")
                .build();

    }
}

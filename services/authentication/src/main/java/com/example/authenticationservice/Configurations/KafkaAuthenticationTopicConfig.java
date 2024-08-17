package com.example.authenticationservice.Configurations;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaAuthenticationTopicConfig {
    @Bean
    public NewTopic authenticationTopic(){
        return TopicBuilder
                .name("authentication-topic")
                .build();

    }
}

package com.example.authenticationservice.Kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaAuthenticationConfig {

    @Bean
    public NewTopic accountCreationTopic() {
        return TopicBuilder.name("account-creation")
                .partitions(1)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic userLoginEventsTopic() {

        return TopicBuilder
                .name("user-login-event")
                .partitions(2)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic passwordChangeEventsTopic() {

        return TopicBuilder
                .name("password-change-event")
                .partitions(1)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic otpVerificationTopic() {

        return TopicBuilder
                .name("otp-verification")
                .partitions(2)
                .replicas(2)
                .build();
    }
}

package com.example.wallet.Kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

public class KafkaWalletConfig {
    @Bean
    public NewTopic transactionConfirmationTopic(){
        return TopicBuilder
                .name("transaction-confirmation")
                .partitions(2)
                .replicas(2)
                .build();

    }
}

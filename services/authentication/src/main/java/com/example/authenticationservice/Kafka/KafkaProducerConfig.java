package com.example.authenticationservice.Kafka;

import com.example.authenticationservice.Kafka.DTOs.AccountCreationEvent;
import com.example.authenticationservice.Kafka.DTOs.OtpVerification;
import com.example.authenticationservice.Kafka.DTOs.PasswordChangeEvent;
import com.example.authenticationservice.Kafka.DTOs.UserLoginEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Bean
    public ProducerFactory<String, AccountCreationEvent> accountCreationEventProducerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, AccountCreationEvent> accountCreationEventKafkaTemplate() {
        return new KafkaTemplate<>(accountCreationEventProducerFactory());
    }

    @Bean
    public ProducerFactory<String, UserLoginEvent> userLoginEventProducerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, UserLoginEvent> userLoginEventKafkaTemplate() {
        return new KafkaTemplate<>(userLoginEventProducerFactory());
    }

    @Bean
    public ProducerFactory<String, PasswordChangeEvent> passwordChangeEventProducerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, PasswordChangeEvent> passwordChangeEventKafkaTemplate() {
        return new KafkaTemplate<>(passwordChangeEventProducerFactory());
    }

    @Bean
    public ProducerFactory<String, OtpVerification> otpVerificationProducerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, OtpVerification> otpVerificationKafkaTemplate() {
        return new KafkaTemplate<>(otpVerificationProducerFactory());
    }
}
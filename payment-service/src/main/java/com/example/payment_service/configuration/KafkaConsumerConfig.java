package com.example.payment_service.configuration;

import com.example.payment_service.entity.Payment;
import com.example.payment_service.service.PaymentEventProducer;
import com.example.payment_service.service.PaymentService;
import events.OrderCreatedEvent;
import java.util.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
@Profile("!local")
@EnableKafka
public class KafkaConsumerConfig {
    private PaymentService paymentService;
    private PaymentEventProducer paymentEventProducer;

    @Bean
    public ConsumerFactory<? super String, ? super OrderCreatedEvent> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "payment-group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaConsumerFactory<>(
            config,
            new StringDeserializer(),
            new JsonDeserializer<>(OrderCreatedEvent.class)
        );
    }

    @KafkaListener(topics = "order.created", groupId = "payment-group")
    public void consume(OrderCreatedEvent event) {

        if (paymentService.alreadyProcessed(event.getEventId())) {
            return; // 🔥 skip duplicate
        }

        Payment payment = paymentService.createFromEvent(event);

        paymentEventProducer.sendPaymentCompletedEvent(payment);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent>
    kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}

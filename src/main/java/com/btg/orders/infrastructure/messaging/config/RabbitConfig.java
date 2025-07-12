package com.btg.orders.infrastructure.messaging.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    
    // Queue names
    public static final String ORDER_QUEUE = "order.queue";
    public static final String ORDER_PROCESSED_QUEUE = "order.processed";
    public static final String ORDER_ERROR_QUEUE = "order.error";
    
    // Exchange names
    public static final String ORDER_EXCHANGE = "order.exchange";
    
    // Routing keys
    public static final String ORDER_ROUTING_KEY = "order.create";
    public static final String ORDER_PROCESSED_ROUTING_KEY = "order.processed";
    public static final String ORDER_ERROR_ROUTING_KEY = "order.error";
    
    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(ORDER_QUEUE).build();
    }
    
    @Bean
    public Queue orderProcessedQueue() {
        return QueueBuilder.durable(ORDER_PROCESSED_QUEUE).build();
    }
    
    @Bean
    public Queue orderErrorQueue() {
        return QueueBuilder.durable(ORDER_ERROR_QUEUE).build();
    }
    
    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }
    
    @Bean
    public Binding orderBinding() {
        return BindingBuilder
            .bind(orderQueue())
            .to(orderExchange())
            .with(ORDER_ROUTING_KEY);
    }
    
    @Bean
    public Binding orderProcessedBinding() {
        return BindingBuilder
            .bind(orderProcessedQueue())
            .to(orderExchange())
            .with(ORDER_PROCESSED_ROUTING_KEY);
    }
    
    @Bean
    public Binding orderErrorBinding() {
        return BindingBuilder
            .bind(orderErrorQueue())
            .to(orderExchange())
            .with(ORDER_ERROR_ROUTING_KEY);
    }
    
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
} 
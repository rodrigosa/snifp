package com.snifp.category_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Fila de saída para o Notification-Service
    public static final String NOTIFICATION_EXCHANGE = "ex.notification";
    public static final String NOTIFICATION_COMMAND_QUEUE = "q.notification-send";
    public static final String NOTIFICATION_ROUTING_KEY = "notification.send";
    public static final String TRANSACTION_CREATED_QUEUE = "q.transaction-created";

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }

    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(NOTIFICATION_COMMAND_QUEUE)
                .withArgument("x-dead-letter-exchange", "ex.notification.dlq")
                .withArgument("x-dead-letter-routing-key", "notification.failed").build();
    }

    @Bean
    public Binding bindingNotification(Queue notificationQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(notificationQueue).to(notificationExchange).with(NOTIFICATION_ROUTING_KEY);
    }

    @Bean
    public Queue transactionCreatedQueue() {
        // Declara explicitamente a fila de entrada para garantir que ela exista ao
        // ligar
        return QueueBuilder.durable(TRANSACTION_CREATED_QUEUE).build();
    }
}

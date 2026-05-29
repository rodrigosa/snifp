package com.snifp.notification_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String NOTIFICATION_QUEUE = "q.notification-send";
    public static final String NOTIFICATION_DLQ = "q.notification-send.dlq";
    public static final String DLQ_EXCHANGE = "ex.notification.dlq";

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    // 1. Declara a Exchange da DLQ (Fila de Erros)
    @Bean
    public TopicExchange dlqExchange() {
        return new TopicExchange(DLQ_EXCHANGE);
    }

    // 2. Declara a Fila DLQ física
    @Bean
    public Queue dlqQueue() {
        return QueueBuilder.durable(NOTIFICATION_DLQ).build();
    }

    // 3. Vincula a fila DLQ à sua Exchange de erro
    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(dlqQueue()).to(dlqExchange()).with("notification.failed");
    }

    // 4. Configura a fila PRINCIPAL para usar a DLQ caso ocorra algum erro no
    // código
    @Bean
    public Queue mainNotificationQueue() {
        return QueueBuilder.durable(NOTIFICATION_QUEUE)
                .withArgument("x-dead-letter-exchange", DLQ_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "notification.failed")
                .build();
    }
}

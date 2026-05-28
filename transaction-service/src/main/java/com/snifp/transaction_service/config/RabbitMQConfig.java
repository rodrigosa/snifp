package com.snifp.transaction_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String TRANSACTION_EXCHANGE = "ex.transaction";
    public static final String TRANSACTION_CREATED_QUEUE = "q.transaction-created";
    public static final String ROUTING_KEY = "transaction.created";

    // BOAS PRÁTICAS: Transforma nossos objetos Java automaticamente em JSON na fila
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public TopicExchange transactionExchange() {
        return new TopicExchange(TRANSACTION_EXCHANGE);
    }

    @Bean
    public Queue transactionCreatedQueue() {
        return QueueBuilder.durable(TRANSACTION_CREATED_QUEUE).build();
    }

    @Bean
    public Binding bindingTransactionCreated(Queue transactionCreatedQueue, TopicExchange transactionExchange) {
        return BindingBuilder.bind(transactionCreatedQueue)
                .to(transactionExchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        // Força a criação das filas declaradas logo no startup do microsserviço
        rabbitAdmin.initialize();
        return rabbitAdmin;
    }

}

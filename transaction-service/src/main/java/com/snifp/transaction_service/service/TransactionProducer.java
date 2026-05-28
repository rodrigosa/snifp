package com.snifp.transaction_service.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.snifp.transaction_service.config.RabbitMQConfig;
import com.snifp.transaction_service.domain.Transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j // Adiciona suporte a logs no console (Boa prática de observabilidade)
public class TransactionProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendTransactionEvent(Transaction transaction) {

        log.info("Publicando evento de transação criada no RabbitMQ para o ID: {}", transaction.getId());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.TRANSACTION_EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                transaction);

        log.info("✅ Evento enviado com sucesso!");
    }

}

package com.snifp.category_service.consumer;

import com.snifp.category_service.dto.TransactionDTO;
import com.snifp.category_service.factory.CategoryFactory;
import com.snifp.category_service.producer.NotificationProducer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionConsumer {

    private final CategoryFactory categoryFactory;
    private final NotificationProducer notificationProducer;

    // Escuta permanentemente a fila criada pelo primeiro microsserviço
    @RabbitListener(queues = "q.transaction-created")
    public void consumeTransaction(TransactionDTO transaction) {
        log.info("Mensagem recebida da fila! Processando transação ID: {}", transaction.getId());
        // log.info("Descrição original: '{}'", transaction.getDescription());

        // Utiliza a Factory + Strategy para descobrir a categoria de forma elegante
        String definedCategory = categoryFactory.categorize(transaction.getDescription());
        transaction.setCategory(definedCategory);

        log.info("Sucesso! Categoria definida para a transação {}: [{}]", transaction.getId(), definedCategory);

        // Dispara para a próxima fila da engrenagem
        notificationProducer.sendToNotificationQueue(transaction);
    }
}

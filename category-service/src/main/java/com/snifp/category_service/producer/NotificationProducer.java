package com.snifp.category_service.producer;

import com.snifp.category_service.config.RabbitMQConfig;
import com.snifp.category_service.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendToNotificationQueue(TransactionDTO transaction) {
        log.info("Encaminhando transação categorizada para a fila de notificações. ID: {}", transaction.getId());

        rabbitTemplate.convertAndSend(RabbitMQConfig.NOTIFICATION_EXCHANGE,
                RabbitMQConfig.NOTIFICATION_ROUTING_KEY,
                transaction);
    }
}

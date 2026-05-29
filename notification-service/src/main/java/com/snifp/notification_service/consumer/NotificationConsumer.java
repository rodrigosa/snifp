package com.snifp.notification_service.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.snifp.notification_service.dto.TransactionDTO;

@Component
@Slf4j
public class NotificationConsumer {

    @RabbitListener(queues = "q.notification-send")
    public void consumeNotification(TransactionDTO transaction) {
        log.info("🔔 Alerta de Finanças recebido para o usuário: {}", transaction.getUserId());

        // Simulação de uma regra de negócio / Validação de erro para testar a DLQ
        if (transaction.getAmount().doubleValue() > 1000.0) {
            log.error("❌ Erro ao processar notificação: Valor suspeito/muito alto. Jogando para a DLQ!");
            throw new IllegalArgumentException("Valor excede o limite seguro de notificações automáticas.");
        }

        log.info("📧 [E-MAIL ENVIADO] Olá, você realizou uma compra de R$ {} em ({}). Categoria: {}.",
                transaction.getAmount(), transaction.getDescription(), transaction.getCategory());
    }
}

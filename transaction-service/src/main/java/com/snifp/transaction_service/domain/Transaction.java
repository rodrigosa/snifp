package com.snifp.transaction_service.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transactions") // Define a coleção no MongoDB
public class Transaction {

    @Id
    private String id; // O MongoDB usa Strings (ObjectId) como padrão para IDs
    private String userId;
    private String description;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String category; // Começará como "PENDENTE" até o category-service atuar

    // BOAS PRÁTICAS/NOSQL: Um mapa flexível para capturar metadados extras
    // que mudam de banco para banco (ex: geolocalização, tags, bandeira do cartão)
    private Map<String, Object> metadata;
}

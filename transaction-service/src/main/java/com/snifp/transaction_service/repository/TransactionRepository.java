package com.snifp.transaction_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.snifp.transaction_service.domain.Transaction;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    // Métodos de busca customizados entrarão aqui no futuro se necessário

}

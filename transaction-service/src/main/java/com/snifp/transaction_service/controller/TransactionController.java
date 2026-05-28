package com.snifp.transaction_service.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snifp.transaction_service.domain.Transaction;
import com.snifp.transaction_service.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/transactions")
@RequiredArgsConstructor // Cria o construtor para injeção de dependência do Lombok
public class TransactionController {

    private final TransactionRepository transactionRepository;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {

        // BOAS PRÁTICAS: Garantir dados essenciais antes de salvar
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setCategory("PENDENTE_CATEGORIZACAO");

        // Salva diretamente no MongoDB Atlas
        Transaction savedTransaction = transactionRepository.save(transaction);

        // Retorna o status 201 (Created) com o objeto salvo (já com o ID gerado pelo
        // Mongo)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTransaction);
    }

}

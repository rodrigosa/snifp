package com.snifp.category_service.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TransactionDTO {

    private String id;
    private String userId;
    private String description;
    private BigDecimal amount;
    private String category;

}

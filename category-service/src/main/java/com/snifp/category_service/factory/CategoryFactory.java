package com.snifp.category_service.factory;

import java.util.List;

import org.springframework.stereotype.Component;

import com.snifp.category_service.strategy.CategoryStrategy;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoryFactory {

    // O Spring injeta todas as classes que implementam CategoryStrategy aqui
    // dentro!
    private final List<CategoryStrategy> strategies;

    public String categorize(String description) {
        return strategies.stream()
                .filter(strategy -> strategy.matches(description))
                .findFirst()
                .map(CategoryStrategy::getCategoryName)
                .orElse("OUTROS"); // Segurança extra
    }
}
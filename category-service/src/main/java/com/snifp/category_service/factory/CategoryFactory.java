package com.snifp.category_service.factory;

import java.util.List;
import java.util.Optional;

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
        return Optional.ofNullable(description) // Embrulha a descrição e trata o nulo com segurança
                .flatMap(desc -> strategies.stream()
                        .filter(strategy -> strategy.matches(desc))
                        .findFirst()
                        .map(CategoryStrategy::getCategoryName))
                .orElse("OUTROS"); // Retorna "OUTROS" se a descrição for nula OU se nenhuma estratégia bater

        // Uso do flatMap: O Optional.ofNullable cria um embrulho seguro. Se
        // description
        // for nulo, ele pula
        // toda a busca nas estratégias e vai direto para o .orElse("OUTROS"). Se não
        // for nulo, o flatMap
        // desembrulha o texto e executa o Stream normalmente.
    }

}
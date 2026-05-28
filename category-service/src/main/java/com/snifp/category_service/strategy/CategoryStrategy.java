package com.snifp.category_service.strategy;

import org.springframework.stereotype.Component;

public interface CategoryStrategy {

    boolean matches(String description); // Verifica se a descrição bate com esta categoria

    String getCategoryName(); // Retorna o nome da categoria (ex: "TRANSPORTE")

    // Estrategia para transporte
    @Component
    public class TransporteStrategy implements CategoryStrategy {

        public boolean matches(String desc) {
            String d = desc.toLowerCase();
            return d.contains("uber") || d.contains("99pop") || d.contains("posto");
        }

        public String getCategoryName() {
            return "TRANSPORTE";
        }

    }

    // Estratégia para Alimentação
    @Component
    public class FoodStrategy implements CategoryStrategy {
        public boolean matches(String desc) {
            String d = desc.toLowerCase();
            return d.contains("ifood") || d.contains("mercado") || d.contains("restaurante");
        }

        public String getCategoryName() {
            return "ALIMENTACAO";
        }
    }

    // Estratégia Padrão (Fallback) se nenhuma outra bater
    // @Component
    public class DefaultStrategy implements CategoryStrategy {
        public boolean matches(String desc) {
            return true;
        }

        public String getCategoryName() {
            return "OUTROS";
        }
    }

}

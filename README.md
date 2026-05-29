# 💰 Sistema de Notificações Inteligentes para Finanças Pessoais

Este projeto consiste em um ecossistema de microsserviços escalável e resiliente projetado para monitorar, categorizar e notificar despesas de usuários em tempo real. A arquitetura foi desenhada seguindo os princípios de sistemas distribuídos e comunicação orientada a eventos (*Event-Driven Architecture*).

## 🛠️ Tecnologias Utilizadas

*   **Linguagem:** Java 21
*   **Framework:** Spring Boot 3.x (Spring Web, Spring Data MongoDB, Spring AMQP)
*   **Mensageria:** RabbitMQ (Docker)
*   **Banco de Dados:** MongoDB Atlas (NoSQL Document Store)
*   **Infraestrutura:** Docker & Docker Compose
*   **Ferramentas:** Lombok, Postman, MongoDB Compass

## 🏗️ Arquitetura do Sistema

O ecossistema é composto por três microsserviços independentes e desacoplados:

1.  **`transaction-service` (Porta 8081):** Recebe as despesas via API REST, persiste os dados no MongoDB de forma flexível e publica o evento `transaction.created` na exchange do RabbitMQ.
2.  **`category-service` (Porta 8082):** Consome os eventos de novas transações, aplica padrões de projeto para classificar a despesa (ex: "Uber" -> `TRANSPORTE`) e encaminha o dado atualizado para a fila de notificações.
3.  **`notification-service` (Porta 8083):** Consome as transações categorizadas e simula o envio de alertas (E-mail/Push). Implementa políticas rígidas de resiliência e tratamento de erros.

---

## 📐 Design Patterns & Boas Práticas Aplicadas

*   **Strategy Pattern:** Utilizado no `category-service` para isolar as regras de categorização (`FoodStrategy`, `TransportStrategy`), eliminando estruturas complexas de `if/else` e tornando o sistema extensível.
*   **Factory Pattern:** Implementado para gerenciar dinamicamente a escolha da estratégia correta de classificação com base na descrição da transação.
*   **Builder Pattern:** Aplicado via Lombok para garantir a criação de objetos e DTOs imutáveis, evitando efeitos colaterais na memória.
*   **Resiliência com DLQ (Dead Letter Queue):** O sistema foi configurado para que falhas definitivas de processamento (como transações suspeitas acima de R$ 1000,00) não causem loops infinitos ou perda de dados. As mensagens rejeitadas são desviadas automaticamente para a fila de auditoria `q.notification-send.dlq`.
*   **Validação Não-Bloqueante com Optional:** Uso de programação funcional e fluxos lineares do Java moderno para manipulação segura de dados nulos.

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
*   Docker e Docker Compose instalados.
*   Maven configurado no ambiente.

### 1. Compilação do Código Java
Na pasta raiz do projeto (`snifp`), execute o comando para compilar e empacotar os três microsserviços:
```bash
(cd transaction-service && mvn clean package) && (cd category-service && mvn clean package) && (cd notification-service && mvn clean package)
```

### 2. Inicialização com Docker Compose
Com os arquivos `.jar` gerados nas pastas `target`, suba todo o ecossistema e a infraestrutura automatizada com um único comando:
```bash
sudo docker compose up --build -d
```

### 3. Acesso aos Painéis
*   **Painel do RabbitMQ:** Acesse [http://localhost:15672](http://localhost:15672) (Usuário: `admin` / Senha: `admin`) para monitorar o tráfego das filas em tempo real.

---

## 🧪 Como Testar os Fluxos

### Fluxo de Sucesso (Cenário Comum)
Envie uma requisição HTTP **POST** para `http://localhost:8081/api/transactions`:
```json
{
  "userId": "user_77",
  "description": "Uber Trip para o trabalho",
  "amount": 35.50
}
```
*   **Resultado esperado:** A despesa será salva no MongoDB como `PENDENTE`, enviada ao RabbitMQ, categorizada pelo `category-service` como `TRANSPORTE`, e o `notification-service` imprimirá o log de e-mail enviado com sucesso.

### Fluxo de Erro Controlado (Ativação da DLQ)
Envie uma despesa com valor abusivo ou suspeito para simular uma falha de negócio:
```json
{
  "userId": "user_77",
  "description": "Notebook Gamer no Mercado",
  "amount": 4500.00
}
```
*   **Resultado esperado:** O `notification-service` rejeitará a mensagem, o log exibirá o erro uma única vez e a mensagem será movida com sucesso para a fila `q.notification-send.dlq` no painel do RabbitMQ.

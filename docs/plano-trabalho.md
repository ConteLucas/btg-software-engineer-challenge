# Plano de Trabalho - Desafio BTG Pactual

## Resumo do Projeto
Desenvolvimento de uma aplicação Java para processamento de pedidos usando Clean Architecture, RabbitMQ, PostgreSQL e Docker.

## Atividades e Estimativas

### 1. Planejamento e Estrutura (4 horas)
- [x] Análise dos requisitos
- [x] Definição da arquitetura
- [x] Criação da estrutura de pastas
- [x] Configuração inicial do projeto

### 2. Camada de Domínio (6 horas)
- [ ] Modelagem das entidades (Order, Client, OrderItem)
- [ ] Implementação dos use cases
- [ ] Criação das interfaces (gateways)
- [ ] Validações de negócio

### 3. Camada de Infraestrutura (8 horas)
- [ ] Configuração do PostgreSQL
- [ ] Implementação dos repositórios
- [ ] Configuração do RabbitMQ
- [ ] Implementação do consumer
- [ ] Mapeamento entidade-modelo

### 4. Camada de Aplicação (4 horas)
- [ ] Implementação dos controllers REST
- [ ] DTOs de request/response
- [ ] Tratamento de exceções
- [ ] Validações de entrada

### 5. Configuração e Deploy (4 horas)
- [ ] Configuração do Docker
- [ ] docker-compose.yml
- [ ] Configurações de ambiente
- [ ] Scripts de inicialização

### 6. Testes (6 horas)
- [ ] Testes unitários das entidades
- [ ] Testes dos use cases
- [ ] Testes de integração
- [ ] Testes dos controllers

### 7. Documentação (4 horas)
- [ ] Diagramas de arquitetura
- [ ] Documentação da API
- [ ] Relatório técnico
- [ ] Evidências de testes

### 8. Finalização (2 horas)
- [ ] Revisão geral
- [ ] Ajustes finais
- [ ] Preparação para entrega

## Total Estimado: 38 horas

## Cronograma
- Dia 1: Planejamento e estrutura
- Dia 2-3: Camada de domínio
- Dia 4-5: Camada de infraestrutura
- Dia 6: Camada de aplicação e testes
- Dia 7: Documentação e finalização

## Tecnologias Utilizadas
- **Linguagem**: Java 17
- **Framework**: Spring Boot 3.x
- **Banco de Dados**: PostgreSQL 15
- **Mensageria**: RabbitMQ
- **Containerização**: Docker & Docker Compose
- **Testes**: JUnit 5, Mockito
- **Build**: Maven
- **Documentação**: Swagger/OpenAPI

## Arquitetura
Clean Architecture com as seguintes camadas:
- **Domain**: Entidades, Use Cases, Gateways
- **Application**: Controllers, DTOs
- **Infrastructure**: Repositories, Consumers, Configurações

## Riscos e Mitigações
- **Risco**: Complexidade da integração RabbitMQ
  - **Mitigação**: Usar Spring AMQP para simplificar
- **Risco**: Configuração do ambiente Docker
  - **Mitigação**: Usar docker-compose para orquestração
- **Risco**: Mapeamento objeto-relacional
  - **Mitigação**: Usar JPA/Hibernate com mapeamentos simples 
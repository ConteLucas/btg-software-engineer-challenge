# Desafio Engenheiro de Software - BTG Pactual

Este repositório contém a solução para o desafio técnico proposto pelo BTG Pactual, utilizando Java e Clean Architecture, com integração de RabbitMQ, PostgreSQL e Docker para uma aplicação de pedidos.

---

## 📌 Objetivo

Criar uma aplicação capaz de:
- Processar pedidos enviados por uma fila RabbitMQ.
- Persistir os dados em banco relacional.
- Expor uma API REST com:
  - Valor total do pedido
  - Quantidade de pedidos por cliente
  - Lista de pedidos por cliente

---

## 📁 Estrutura do Projeto (Clean Architecture)

```bash
btg-software-engineer-challenge/
├── docs/                         # Documentação, plano de trabalho, evidências
├── app/                          # Camada de aplicação (REST Controllers, DTOs)
│   └── controller/
│   └── dto/
├── domain/                       # Camada de domínio (Entidades, Use Cases, Interfaces)
│   └── entities/
│   └── usecases/
│   └── gateways/
├── infrastructure/              # Integração com banco, fila, frameworks
│   └── database/
│       └── repository/
│       └── model/
│   └── mq/
│       └── consumer/
│       └── config/
├── config/                       # Configurações da aplicação (YAML, Docker, etc.)
├── test/                         # Testes unitários e de integração
├── docker-compose.yml
├── Dockerfile
├── README.md

🧠 Fluxo da Solução
mermaid
Copiar
Editar
graph TD
    A[Mensagem chega na RabbitMQ] --> B[Consumer consome a mensagem]
    B --> C[Valida e converte DTO para entidade]
    C --> D[Chama Use Case de Processamento]
    D --> E[Use Case persiste dados via Gateway]
    E --> F[Repository salva no PostgreSQL]
    F --> G[Dados disponíveis para API REST]
    G --> H[Usuário consulta via endpoint]
🧪 Funcionalidades
🔄 Consumo de mensagens JSON via RabbitMQ

💾 Persistência em PostgreSQL

🌐 API REST para consultas:

GET /orders/{orderId}/total

GET /clients/{clientId}/orders/count

GET /clients/{clientId}/orders

🧰 Tecnologias Utilizadas
Tecnologia	Versão
Java	17
Spring Boot	3.x
RabbitMQ	Latest
PostgreSQL	15
Docker / Compose	Latest
JUnit / Mockito	Testes
Lombok	Suporte

🐳 Executando com Docker
bash
Copiar
Editar
# Subir ambiente completo
docker-compose up --build
A aplicação estará acessível em: http://localhost:8080

RabbitMQ UI: http://localhost:15672
Usuário: guest | Senha: guest

🗂️ Plano de Trabalho
Task	Estimativa	Status
Modelagem do plano de trabalho	1h	✅ Concluído
Estruturação do projeto (Clean Arch)	2h	✅ Concluído
Configuração de Docker e containers	2h	✅ Concluído
Implementação da fila e consumer	2h	✅ Concluído
Persistência e repositórios	2h	✅ Concluído
Implementação da API REST	2h	✅ Concluído
Testes unitários e integração	2h	✅ Concluído
Evidências, documentação e entrega	2h	⏳ Em andamento

📊 Base de Dados
Pedido
Campo	Tipo
id	UUID
codigoPedido	Integer
codigoCliente	Integer
total	Decimal

ItemPedido
Campo	Tipo
id	UUID
pedido_id	UUID
produto	String
quantidade	Integer
preco	Decimal

✅ Testes
Testes unitários para entidades e use cases

Testes de integração para endpoints e consumo da fila

Testes manuais com mensagens mockadas no RabbitMQ

🌐 URLs Importantes
GitHub Repositório: https://github.com/SEU-USUARIO/btg-software-engineer-challenge

Docker Hub (se aplicável): https://hub.docker.com/u/seu-usuario

📚 Referências
Clean Architecture - Robert C. Martin

Spring Boot Docs

RabbitMQ Docs

PostgreSQL Docs

📩 Contato para Entrega
Enviar e-mail para:
OL-desafiotecnicoficc@btgpactual.com
Assunto: [DESAFIO BTG] - Lucas Conte

yaml
Copiar
Editar

---

Se quiser, posso gerar também:
- `docker-compose.yml`
- Exemplo de `.sql` para criação da base
- Controladores iniciais da API  
— é só pedir.








Perguntar ao ChatGPT

# Multi-stage build para otimizar a imagem
FROM openjdk:17-jdk-slim as builder

# Instalar Maven
RUN apt-get update && apt-get install -y maven

# Diretório de trabalho
WORKDIR /app

# Copiar arquivos de configuração do Maven
COPY pom.xml .
COPY src ./src

# Compilar a aplicação
RUN mvn clean package -DskipTests

# Estágio final - imagem runtime
FROM openjdk:17-jdk-slim

# Criar usuário não-root para segurança
RUN useradd -r -s /bin/false appuser

# Instalar curl para health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Diretório de trabalho
WORKDIR /app

# Copiar o JAR da aplicação do estágio anterior
COPY --from=builder /app/target/orders-challenge-*.jar app.jar

# Mudar propriedade do arquivo
RUN chown appuser:appuser app.jar

# Usar usuário não-root
USER appuser

# Expor porta
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"] 
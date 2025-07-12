#!/bin/bash

# Desafio BTG Pactual - Script de Inicialização
# Autor: [Seu Nome]

echo "🚀 Iniciando aplicação BTG Orders Challenge..."
echo "=" * 60

# Verificar se Docker está rodando
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker não está rodando. Por favor, inicie o Docker primeiro."
    exit 1
fi

# Verificar se docker-compose está disponível
if ! command -v docker-compose &> /dev/null; then
    echo "❌ docker-compose não encontrado. Por favor, instale o docker-compose."
    exit 1
fi

# Função para verificar se porta está em uso
check_port() {
    local port=$1
    if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null 2>&1; then
        echo "⚠️  Porta $port já está em uso. Parando o serviço..."
        lsof -ti:$port | xargs kill -9 2>/dev/null || true
        sleep 2
    fi
}

# Verificar portas necessárias
echo "🔍 Verificando portas necessárias..."
check_port 8080
check_port 5432
check_port 5672
check_port 15672

# Limpar containers antigos se existirem
echo "🧹 Limpando containers antigos..."
docker-compose down -v 2>/dev/null || true

# Construir e subir os serviços
echo "🏗️  Construindo e iniciando serviços..."
docker-compose up --build -d

# Aguardar serviços ficarem prontos
echo "⏳ Aguardando serviços inicializarem..."
sleep 30

# Verificar se os serviços estão rodando
echo "🔍 Verificando status dos serviços..."
docker-compose ps

# Verificar health checks
echo "🏥 Verificando health checks..."
sleep 10

# Tentar acessar a aplicação
echo "🌐 Testando conectividade..."
if curl -f http://localhost:8080/actuator/health > /dev/null 2>&1; then
    echo "✅ Aplicação está rodando corretamente!"
else
    echo "⚠️  Aplicação pode estar ainda inicializando. Aguarde mais alguns segundos."
fi

echo ""
echo "📋 Resumo dos serviços:"
echo "   🌐 Aplicação: http://localhost:8080"
echo "   📚 Swagger UI: http://localhost:8080/swagger-ui.html"
echo "   🐰 RabbitMQ Management: http://localhost:15672 (guest/guest)"
echo "   💾 PostgreSQL: localhost:5432 (postgres/postgres)"
echo ""
echo "🧪 Para testar a aplicação:"
echo "   1. Instale Python: pip install pika"
echo "   2. Envie pedidos: python scripts/send-test-order.py"
echo "   3. Acesse Swagger UI para testar as APIs"
echo ""
echo "📊 Para monitorar:"
echo "   - Logs: docker-compose logs -f"
echo "   - Status: docker-compose ps"
echo "   - Parar: docker-compose down"
echo ""
echo "🎉 Aplicação iniciada com sucesso!"
echo "=" * 60 
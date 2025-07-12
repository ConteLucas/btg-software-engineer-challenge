#!/usr/bin/env python3
"""
Script para enviar pedidos de teste para a fila RabbitMQ
Uso: python send-test-order.py
"""

import pika
import json
import sys
from datetime import datetime

def send_test_order():
    """Envia um pedido de teste para a fila RabbitMQ"""
    
    # Configuração do RabbitMQ
    connection_params = pika.ConnectionParameters(
        host='localhost',
        port=5672,
        virtual_host='/',
        credentials=pika.PlainCredentials('guest', 'guest')
    )
    
    try:
        # Conectar ao RabbitMQ
        connection = pika.BlockingConnection(connection_params)
        channel = connection.channel()
        
        # Declarar a fila
        channel.queue_declare(queue='order.queue', durable=True)
        
        # Mensagem de teste baseada no exemplo do requisito
        test_order = {
            "codigoPedido": 1001,
            "codigoCliente": 1,
            "itens": [
                {
                    "produto": "lápis",
                    "quantidade": 100,
                    "preco": 1.10
                },
                {
                    "produto": "caderno",
                    "quantidade": 10,
                    "preco": 1.00
                }
            ]
        }
        
        # Converter para JSON
        message = json.dumps(test_order)
        
        # Enviar mensagem
        channel.basic_publish(
            exchange='',
            routing_key='order.queue',
            body=message,
            properties=pika.BasicProperties(
                delivery_mode=2,  # Tornar a mensagem persistente
                content_type='application/json'
            )
        )
        
        print(f"✅ Pedido enviado com sucesso!")
        print(f"📝 Código do pedido: {test_order['codigoPedido']}")
        print(f"👤 Cliente: {test_order['codigoCliente']}")
        print(f"📦 Itens: {len(test_order['itens'])}")
        print(f"💰 Valor total esperado: R$ {calculate_total(test_order['itens']):.2f}")
        
        # Fechar conexão
        connection.close()
        
    except Exception as e:
        print(f"❌ Erro ao enviar pedido: {str(e)}")
        sys.exit(1)

def calculate_total(items):
    """Calcula o total do pedido"""
    total = 0
    for item in items:
        total += item['quantidade'] * item['preco']
    return total

def send_multiple_orders():
    """Envia múltiplos pedidos para teste"""
    
    orders = [
        {
            "codigoPedido": 1002,
            "codigoCliente": 2,
            "itens": [
                {"produto": "notebook", "quantidade": 1, "preco": 2500.00},
                {"produto": "mouse", "quantidade": 1, "preco": 50.00}
            ]
        },
        {
            "codigoPedido": 1003,
            "codigoCliente": 1,
            "itens": [
                {"produto": "teclado", "quantidade": 1, "preco": 150.00}
            ]
        },
        {
            "codigoPedido": 1004,
            "codigoCliente": 3,
            "itens": [
                {"produto": "monitor", "quantidade": 2, "preco": 800.00},
                {"produto": "cabo HDMI", "quantidade": 2, "preco": 25.00}
            ]
        }
    ]
    
    connection_params = pika.ConnectionParameters(
        host='localhost',
        port=5672,
        virtual_host='/',
        credentials=pika.PlainCredentials('guest', 'guest')
    )
    
    try:
        connection = pika.BlockingConnection(connection_params)
        channel = connection.channel()
        channel.queue_declare(queue='order.queue', durable=True)
        
        for order in orders:
            message = json.dumps(order)
            channel.basic_publish(
                exchange='',
                routing_key='order.queue',
                body=message,
                properties=pika.BasicProperties(
                    delivery_mode=2,
                    content_type='application/json'
                )
            )
            print(f"✅ Pedido {order['codigoPedido']} enviado para cliente {order['codigoCliente']}")
        
        connection.close()
        print(f"🎉 Todos os {len(orders)} pedidos foram enviados com sucesso!")
        
    except Exception as e:
        print(f"❌ Erro ao enviar pedidos: {str(e)}")
        sys.exit(1)

if __name__ == "__main__":
    print("🚀 Enviando pedidos de teste para RabbitMQ...")
    print("=" * 50)
    
    if len(sys.argv) > 1 and sys.argv[1] == "multiple":
        send_multiple_orders()
    else:
        send_test_order()
    
    print("=" * 50)
    print("💡 Dicas:")
    print("- Verifique os logs da aplicação para ver o processamento")
    print("- Acesse http://localhost:8080/swagger-ui.html para testar as APIs")
    print("- Use 'python send-test-order.py multiple' para enviar vários pedidos") 
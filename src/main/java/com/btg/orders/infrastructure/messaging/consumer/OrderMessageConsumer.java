package com.btg.orders.infrastructure.messaging.consumer;

import com.btg.orders.domain.usecases.ProcessOrderUseCase;
import com.btg.orders.domain.usecases.interfaces.ProcessOrderUseCaseInterface;
import com.btg.orders.infrastructure.messaging.config.RabbitConfig;
import com.btg.orders.infrastructure.messaging.consumer.interfaces.MessageConsumerInterface;
import com.btg.orders.infrastructure.messaging.dto.OrderMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderMessageConsumer implements MessageConsumerInterface {
    
    private final ProcessOrderUseCaseInterface processOrderUseCase;
    
    @RabbitListener(queues = RabbitConfig.ORDER_QUEUE)
    public void handleOrderMessage(OrderMessageDto message) {
        log.info("Received order message: {}", message);
        
        try {
            // Convert DTO to use case input
            List<ProcessOrderUseCase.OrderItemData> items = message.getItens().stream()
                .map(item -> ProcessOrderUseCase.OrderItemData.builder()
                    .product(item.getProduto())
                    .quantity(item.getQuantidade())
                    .price(item.getPreco())
                    .build())
                .collect(Collectors.toList());
            
            // Process the order
            processOrderUseCase.execute(
                message.getCodigoPedido(),
                message.getCodigoCliente(),
                items
            );
            
            log.info("Order processed successfully: {}", message.getCodigoPedido());
            
        } catch (Exception e) {
            log.error("Error processing order {}: {}", message.getCodigoPedido(), e.getMessage(), e);
            // Exception handling is done in the use case
        }
    }
} 
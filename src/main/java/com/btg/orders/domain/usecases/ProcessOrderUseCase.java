package com.btg.orders.domain.usecases;

import com.btg.orders.domain.entities.Order;
import com.btg.orders.domain.entities.OrderItem;
import com.btg.orders.domain.gateways.ClientGateway;
import com.btg.orders.domain.gateways.OrderGateway;
import com.btg.orders.domain.gateways.MessageGateway;
import com.btg.orders.domain.services.interfaces.EventPublisherServiceInterface;
import com.btg.orders.domain.services.interfaces.OrderValidationServiceInterface;
import com.btg.orders.domain.usecases.interfaces.ProcessOrderUseCaseInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessOrderUseCase implements ProcessOrderUseCaseInterface {
    
    private final OrderGateway orderGateway;
    private final ClientGateway clientGateway;
    private final MessageGateway messageGateway;
    private final OrderValidationServiceInterface validationService;
    private final EventPublisherServiceInterface eventPublisher;
    
    public Order execute(Long orderCode, Long clientId, List<OrderItemData> items) {
        log.info("Processing order with code: {} for client: {}", orderCode, clientId);
        
        try {
            // Valida o pedido antes de processar
            validationService.validateOrderForProcessing(orderCode, clientId, items);
            
            // Verifica se o cliente existe, se não, cria um cliente padrão
            clientGateway.findOrCreateDefaultClient(clientId);
            
            // Cria o pedido
            Order order = Order.builder()
                .orderCode(orderCode)
                .clientId(clientId)
                .items(new java.util.ArrayList<>())
                .total(java.math.BigDecimal.ZERO)
                .createdAt(java.time.LocalDateTime.now())
                .build();
            
            // Adiciona os itens ao pedido
            for (OrderItemData itemData : items) {
                OrderItem item = OrderItem.builder()
                    .product(itemData.getProduct())
                    .quantity(itemData.getQuantity())
                    .price(itemData.getPrice())
                    .build();
                item.updateTotal();
                order.addItem(item);
            }
            
            // Calcula o total
            order.updateTotal();
            
            // Valida o pedido processado
            validationService.validateProcessedOrder(order);
            
            // Salva o pedido
            Order savedOrder = orderGateway.save(order);
            
            // Envia notificação de sucesso
            messageGateway.sendOrderProcessedNotification(orderCode);
            eventPublisher.publishOrderProcessedEvent(savedOrder);
            
            log.info("Order {} processed successfully with total: {}", orderCode, savedOrder.getTotal());
            
            return savedOrder;
            
        } catch (Exception e) {
            log.error("Error processing order {}: {}", orderCode, e.getMessage());
            messageGateway.sendOrderErrorNotification(orderCode, e.getMessage());
            eventPublisher.publishOrderErrorEvent(orderCode, e.getMessage());
            throw e;
        }
    }
    
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    @lombok.experimental.FieldDefaults(level = lombok.AccessLevel.PRIVATE)
    public static class OrderItemData {
        String product;
        Integer quantity;
        BigDecimal price;
    }
} 
package com.btg.orders.domain.services;

import com.btg.orders.domain.entities.Order;
import com.btg.orders.domain.gateways.OrderGateway;
import com.btg.orders.domain.services.interfaces.EventPublisherServiceInterface;
import com.btg.orders.domain.services.interfaces.OrderValidationServiceInterface;
import com.btg.orders.domain.services.interfaces.TransactionalServiceInterface;
import com.btg.orders.domain.usecases.ProcessOrderUseCase;
import com.btg.orders.domain.usecases.interfaces.ProcessOrderUseCaseInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionalService implements TransactionalServiceInterface {
    
    private final ProcessOrderUseCaseInterface processOrderUseCase;
    private final OrderValidationServiceInterface validationService;
    private final EventPublisherServiceInterface eventPublisher;
    private final OrderGateway orderGateway;
    
    @Override
    @Transactional
    public Order processOrderTransactionally(Long orderCode, Long clientId, List<ProcessOrderUseCase.OrderItemData> items) {
        log.info("Processing order transactionally: orderCode={}, clientId={}", orderCode, clientId);
        
        try {
            // Validate order before processing
            validationService.validateOrderForProcessing(orderCode, clientId, items);
            eventPublisher.publishOrderValidationEvent(orderCode, clientId, true);
            
            // Process the order
            Order processedOrder = processOrderUseCase.execute(orderCode, clientId, items);
            
            // Validate processed order
            validationService.validateProcessedOrder(processedOrder);
            
            // Publish success event
            eventPublisher.publishOrderCreatedEvent(processedOrder);
            eventPublisher.publishOrderProcessedEvent(processedOrder);
            
            log.info("Order processed successfully in transaction: orderCode={}", orderCode);
            return processedOrder;
            
        } catch (Exception e) {
            log.error("Error processing order transactionally: orderCode={}, error={}", orderCode, e.getMessage());
            
            // Publish error event
            eventPublisher.publishOrderValidationEvent(orderCode, clientId, false);
            eventPublisher.publishOrderErrorEvent(orderCode, e.getMessage());
            
            throw e;
        }
    }
    
    @Override
    @Transactional
    public void rollbackOrderProcessing(Long orderCode) {
        log.warn("Rolling back order processing: orderCode={}", orderCode);
        
        try {
            // Find and delete the order if it exists
            var order = orderGateway.findByOrderCode(orderCode);
            if (order.isPresent()) {
                orderGateway.deleteById(order.get().getId());
                log.info("Order rolled back successfully: orderCode={}", orderCode);
                
                // Publish rollback event
                eventPublisher.publishOrderErrorEvent(orderCode, "Order processing rolled back");
            }
            
        } catch (Exception e) {
            log.error("Error rolling back order: orderCode={}, error={}", orderCode, e.getMessage());
            throw e;
        }
    }
    
    @Override
    public <T> T executeWithRetry(java.util.function.Supplier<T> operation, int maxRetries) {
        log.debug("Executing operation with retry: maxRetries={}", maxRetries);
        
        Exception lastException = null;
        
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                log.debug("Attempt {}/{}", attempt, maxRetries);
                return operation.get();
                
            } catch (Exception e) {
                lastException = e;
                log.warn("Operation failed on attempt {}/{}: {}", attempt, maxRetries, e.getMessage());
                
                if (attempt < maxRetries) {
                    try {
                        // Wait before retry (exponential backoff)
                        long waitTime = 1000L * (long) Math.pow(2, attempt - 1);
                        Thread.sleep(waitTime);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Operation interrupted", ie);
                    }
                }
            }
        }
        
        log.error("Operation failed after {} attempts", maxRetries);
        throw new RuntimeException("Operation failed after " + maxRetries + " attempts", lastException);
    }
} 
package com.btg.orders.domain.services;

import com.btg.orders.domain.entities.Order;
import com.btg.orders.domain.gateways.OrderGateway;
import com.btg.orders.domain.services.interfaces.OrderValidationServiceInterface;
import com.btg.orders.domain.usecases.ProcessOrderUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderValidationService implements OrderValidationServiceInterface {
    
    private final OrderGateway orderGateway;
    
    @Override
    public void validateOrderForProcessing(Long orderCode, Long clientId, List<ProcessOrderUseCase.OrderItemData> items) {
        if (orderCode == null || orderCode <= 0) {
            throw new IllegalArgumentException("Order code must be positive");
        }
        
        if (clientId == null || clientId <= 0) {
            throw new IllegalArgumentException("Client ID must be positive");
        }
        
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }
        
        if (orderExists(orderCode)) {
            throw new IllegalArgumentException("Order with code " + orderCode + " already exists");
        }
        
        for (ProcessOrderUseCase.OrderItemData item : items) {
            validateOrderItem(item);
        }
    }
    
    @Override
    public void validateProcessedOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        
        if (order.getOrderCode() == null || order.getOrderCode() <= 0) {
            throw new IllegalArgumentException("Order code must be positive");
        }
        
        if (order.getClientId() == null || order.getClientId() <= 0) {
            throw new IllegalArgumentException("Client ID must be positive");
        }
        
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }
        
        if (order.getTotal() == null || order.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Order total must be positive");
        }
    }
    
    @Override
    public boolean orderExists(Long orderCode) {
        return orderGateway.existsByOrderCode(orderCode);
    }
    
    private void validateOrderItem(ProcessOrderUseCase.OrderItemData item) {
        if (item == null) {
            throw new IllegalArgumentException("Order item cannot be null");
        }
        
        if (item.getProduct() == null || item.getProduct().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        
        if (item.getQuantity() == null || item.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        if (item.getPrice() == null || item.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
    }
} 
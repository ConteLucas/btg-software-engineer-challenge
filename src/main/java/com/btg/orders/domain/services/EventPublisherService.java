package com.btg.orders.domain.services;

import com.btg.orders.domain.entities.Order;
import com.btg.orders.domain.gateways.MessageGateway;
import com.btg.orders.domain.services.interfaces.EventPublisherServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EventPublisherService implements EventPublisherServiceInterface {
    
    private final MessageGateway messageGateway;
    
    @Override
    public void publishOrderProcessedEvent(Order order) {
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("orderCode", order.getOrderCode());
        eventData.put("clientId", order.getClientId());
        eventData.put("total", order.getTotal());
        eventData.put("itemCount", order.getItems().size());
        eventData.put("timestamp", order.getCreatedAt());
        eventData.put("eventType", "ORDER_PROCESSED");
        
        messageGateway.sendMessage("order.processed", eventData);
    }
    
    @Override
    public void publishOrderErrorEvent(Long orderCode, String error) {
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("orderCode", orderCode);
        eventData.put("error", error);
        eventData.put("timestamp", java.time.LocalDateTime.now());
        eventData.put("eventType", "ORDER_ERROR");
        
        messageGateway.sendMessage("order.error", eventData);
    }
    
    @Override
    public void publishOrderCreatedEvent(Order order) {
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("orderCode", order.getOrderCode());
        eventData.put("clientId", order.getClientId());
        eventData.put("total", order.getTotal());
        eventData.put("timestamp", order.getCreatedAt());
        eventData.put("eventType", "ORDER_CREATED");
        
        messageGateway.sendMessage("order.created", eventData);
    }
    
    @Override
    public void publishOrderValidationEvent(Long orderCode, Long clientId, boolean isValid) {
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("orderCode", orderCode);
        eventData.put("clientId", clientId);
        eventData.put("isValid", isValid);
        eventData.put("timestamp", java.time.LocalDateTime.now());
        eventData.put("eventType", "ORDER_VALIDATION");
        
        messageGateway.sendMessage("order.validation", eventData);
    }
} 
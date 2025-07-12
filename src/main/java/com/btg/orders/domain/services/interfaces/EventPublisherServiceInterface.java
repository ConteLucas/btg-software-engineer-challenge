package com.btg.orders.domain.services.interfaces;

import com.btg.orders.domain.entities.Order;

public interface EventPublisherServiceInterface {
    void publishOrderProcessedEvent(Order order);
    void publishOrderErrorEvent(Long orderCode, String error);
    void publishOrderCreatedEvent(Order order);
    void publishOrderValidationEvent(Long orderCode, Long clientId, boolean isValid);
} 
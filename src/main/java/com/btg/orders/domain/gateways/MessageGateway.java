package com.btg.orders.domain.gateways;

public interface MessageGateway {
    
    void sendMessage(String queue, Object message);
    
    void sendOrderProcessedNotification(Long orderCode);
    
    void sendOrderErrorNotification(Long orderCode, String error);
} 
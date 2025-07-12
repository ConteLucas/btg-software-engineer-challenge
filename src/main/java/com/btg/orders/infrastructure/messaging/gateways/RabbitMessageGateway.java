package com.btg.orders.infrastructure.messaging.gateways;

import com.btg.orders.domain.gateways.MessageGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMessageGateway implements MessageGateway {
    
    private final RabbitTemplate rabbitTemplate;
    
    @Override
    public void sendMessage(String queue, Object message) {
        log.info("Sending message to queue: {}", queue);
        
        try {
            rabbitTemplate.convertAndSend(queue, message);
            log.info("Message sent successfully to queue: {}", queue);
        } catch (Exception e) {
            log.error("Error sending message to queue {}: {}", queue, e.getMessage());
            throw new RuntimeException("Failed to send message to queue: " + queue, e);
        }
    }
    
    @Override
    public void sendOrderProcessedNotification(Long orderCode) {
        log.info("Sending order processed notification for order: {}", orderCode);
        
        String message = String.format("Order %d processed successfully", orderCode);
        sendMessage("order.processed", message);
    }
    
    @Override
    public void sendOrderErrorNotification(Long orderCode, String error) {
        log.error("Sending order error notification for order: {} - Error: {}", orderCode, error);
        
        String message = String.format("Order %d processing failed: %s", orderCode, error);
        sendMessage("order.error", message);
    }
} 
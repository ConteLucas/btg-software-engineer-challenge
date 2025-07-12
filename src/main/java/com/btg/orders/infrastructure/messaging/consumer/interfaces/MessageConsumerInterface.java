package com.btg.orders.infrastructure.messaging.consumer.interfaces;

import com.btg.orders.infrastructure.messaging.dto.OrderMessageDto;

public interface MessageConsumerInterface {
    void handleOrderMessage(OrderMessageDto message);
} 
package com.btg.orders.domain.services.interfaces;

import com.btg.orders.domain.entities.Order;
import com.btg.orders.domain.usecases.ProcessOrderUseCase;

import java.util.List;

public interface OrderValidationServiceInterface {
    void validateOrderForProcessing(Long orderCode, Long clientId, List<ProcessOrderUseCase.OrderItemData> items);
    void validateProcessedOrder(Order order);
    boolean orderExists(Long orderCode);
} 
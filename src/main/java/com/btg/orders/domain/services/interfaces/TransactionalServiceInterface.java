package com.btg.orders.domain.services.interfaces;

import com.btg.orders.domain.entities.Order;
import com.btg.orders.domain.usecases.ProcessOrderUseCase;

import java.util.List;

public interface TransactionalServiceInterface {
    Order processOrderTransactionally(Long orderCode, Long clientId, List<ProcessOrderUseCase.OrderItemData> items);
    void rollbackOrderProcessing(Long orderCode);
    <T> T executeWithRetry(java.util.function.Supplier<T> operation, int maxRetries);
} 
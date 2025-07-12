package com.btg.orders.domain.usecases.interfaces;

import com.btg.orders.domain.entities.Order;
import com.btg.orders.domain.usecases.ProcessOrderUseCase;

import java.util.List;

public interface ProcessOrderUseCaseInterface {
    Order execute(Long orderCode, Long clientId, List<ProcessOrderUseCase.OrderItemData> items);
} 
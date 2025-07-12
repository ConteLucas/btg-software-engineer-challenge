package com.btg.orders.domain.usecases.interfaces;

import com.btg.orders.domain.entities.Order;

import java.util.List;

public interface GetOrdersByClientUseCaseInterface {
    List<Order> execute(Long clientId);
} 
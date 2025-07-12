package com.btg.orders.domain.gateways;

import com.btg.orders.domain.entities.Order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderGateway {
    
    Order save(Order order);
    
    Optional<Order> findById(Long id);
    
    Optional<Order> findByOrderCode(Long orderCode);
    
    List<Order> findByClientId(Long clientId);
    
    Optional<BigDecimal> calculateOrderTotal(Long orderCode);
    
    Long countOrdersByClient(Long clientId);
    
    List<Order> findAll();
    
    void deleteById(Long id);
    
    boolean existsByOrderCode(Long orderCode);
} 
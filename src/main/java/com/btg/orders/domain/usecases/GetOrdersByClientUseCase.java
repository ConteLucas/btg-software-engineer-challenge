package com.btg.orders.domain.usecases;

import com.btg.orders.domain.entities.Order;
import com.btg.orders.domain.gateways.OrderGateway;
import com.btg.orders.domain.usecases.interfaces.GetOrdersByClientUseCaseInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetOrdersByClientUseCase implements GetOrdersByClientUseCaseInterface {
    
    private final OrderGateway orderGateway;
    
    public List<Order> execute(Long clientId) {
        log.info("Getting orders for client: {}", clientId);
        
        return orderGateway.findByClientId(clientId);
    }
} 
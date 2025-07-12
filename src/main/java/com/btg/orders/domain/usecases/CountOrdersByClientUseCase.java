package com.btg.orders.domain.usecases;

import com.btg.orders.domain.gateways.OrderGateway;
import com.btg.orders.domain.usecases.interfaces.CountOrdersByClientUseCaseInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CountOrdersByClientUseCase implements CountOrdersByClientUseCaseInterface {
    
    private final OrderGateway orderGateway;
    
    public Long execute(Long clientId) {
        log.info("Counting orders for client: {}", clientId);
        
        return orderGateway.countOrdersByClient(clientId);
    }
} 
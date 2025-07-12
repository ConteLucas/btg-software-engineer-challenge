package com.btg.orders.domain.usecases;

import com.btg.orders.domain.gateways.OrderGateway;
import com.btg.orders.domain.usecases.interfaces.GetOrderTotalUseCaseInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetOrderTotalUseCase implements GetOrderTotalUseCaseInterface {
    
    private final OrderGateway orderGateway;
    
    public BigDecimal execute(Long orderCode) {
        log.info("Getting total for order: {}", orderCode);
        
        return orderGateway.calculateOrderTotal(orderCode)
            .orElseThrow(() -> new IllegalArgumentException("Order not found with code: " + orderCode));
    }
} 
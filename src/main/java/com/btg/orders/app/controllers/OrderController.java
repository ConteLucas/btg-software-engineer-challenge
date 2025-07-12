package com.btg.orders.app.controllers;

import com.btg.orders.app.controllers.interfaces.OrderControllerInterface;
import com.btg.orders.app.dto.OrderCountResponseDto;
import com.btg.orders.app.dto.OrderResponseDto;
import com.btg.orders.app.dto.OrderTotalResponseDto;
import com.btg.orders.app.mappers.OrderDtoMapper;
import com.btg.orders.domain.entities.Order;
import com.btg.orders.domain.usecases.interfaces.CountOrdersByClientUseCaseInterface;
import com.btg.orders.domain.usecases.interfaces.GetOrdersByClientUseCaseInterface;
import com.btg.orders.domain.usecases.interfaces.GetOrderTotalUseCaseInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController implements OrderControllerInterface {
    
    private final GetOrderTotalUseCaseInterface getOrderTotalUseCase;
    private final CountOrdersByClientUseCaseInterface countOrdersByClientUseCase;
    private final GetOrdersByClientUseCaseInterface getOrdersByClientUseCase;
    private final OrderDtoMapper orderDtoMapper;
    
    @GetMapping("/{orderCode}/total")
    public ResponseEntity<OrderTotalResponseDto> getOrderTotal(@PathVariable Long orderCode) {
        log.info("Getting total for order: {}", orderCode);
        
        try {
            BigDecimal total = getOrderTotalUseCase.execute(orderCode);
            OrderTotalResponseDto response = new OrderTotalResponseDto(orderCode, total);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.warn("Order not found: {}", orderCode);
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/clients/{clientId}/count")
    public ResponseEntity<OrderCountResponseDto> countOrdersByClient(@PathVariable Long clientId) {
        log.info("Counting orders for client: {}", clientId);
        
        Long count = countOrdersByClientUseCase.execute(clientId);
        OrderCountResponseDto response = new OrderCountResponseDto(clientId, count);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/clients/{clientId}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByClient(@PathVariable Long clientId) {
        log.info("Getting orders for client: {}", clientId);
        
        List<Order> orders = getOrdersByClientUseCase.execute(clientId);
        List<OrderResponseDto> response = orderDtoMapper.toResponseDtoList(orders);
        return ResponseEntity.ok(response);
    }
} 
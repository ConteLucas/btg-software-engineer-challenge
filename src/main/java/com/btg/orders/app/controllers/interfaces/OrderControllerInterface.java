package com.btg.orders.app.controllers.interfaces;

import com.btg.orders.app.dto.OrderCountResponseDto;
import com.btg.orders.app.dto.OrderResponseDto;
import com.btg.orders.app.dto.OrderTotalResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderControllerInterface {
    
    ResponseEntity<OrderTotalResponseDto> getOrderTotal(Long orderCode);
    
    ResponseEntity<OrderCountResponseDto> countOrdersByClient(Long clientId);
    
    ResponseEntity<List<OrderResponseDto>> getOrdersByClient(Long clientId);
} 
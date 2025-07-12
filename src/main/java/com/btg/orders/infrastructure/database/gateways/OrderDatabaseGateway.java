package com.btg.orders.infrastructure.database.gateways;

import com.btg.orders.domain.entities.Order;
import com.btg.orders.domain.gateways.OrderGateway;
import com.btg.orders.infrastructure.database.mappers.OrderMapper;
import com.btg.orders.infrastructure.database.models.OrderModel;
import com.btg.orders.infrastructure.database.repositories.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderDatabaseGateway implements OrderGateway {
    
    private final OrderJpaRepository repository;
    private final OrderMapper mapper;
    
    @Override
    public Order save(Order order) {
        log.info("Saving order: {}", order.getOrderCode());
        
        OrderModel model = mapper.toModel(order);
        OrderModel savedModel = repository.save(model);
        
        return mapper.toDomain(savedModel);
    }
    
    @Override
    public Optional<Order> findById(Long id) {
        log.info("Finding order by id: {}", id);
        
        return repository.findById(id)
            .map(mapper::toDomain);
    }
    
    @Override
    public Optional<Order> findByOrderCode(Long orderCode) {
        log.info("Finding order by order code: {}", orderCode);
        
        return repository.findByOrderCode(orderCode)
            .map(mapper::toDomain);
    }
    
    @Override
    public List<Order> findByClientId(Long clientId) {
        log.info("Finding orders by client id: {}", clientId);
        
        return repository.findByClientId(clientId).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public Optional<BigDecimal> calculateOrderTotal(Long orderCode) {
        log.info("Calculating total for order: {}", orderCode);
        
        return repository.findTotalByOrderCode(orderCode);
    }
    
    @Override
    public Long countOrdersByClient(Long clientId) {
        log.info("Counting orders by client: {}", clientId);
        
        return repository.countByClientId(clientId);
    }
    
    @Override
    public List<Order> findAll() {
        log.info("Finding all orders");
        
        return repository.findAll().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        log.info("Deleting order by id: {}", id);
        
        repository.deleteById(id);
    }
    
    @Override
    public boolean existsByOrderCode(Long orderCode) {
        log.info("Checking if order exists by code: {}", orderCode);
        
        return repository.existsByOrderCode(orderCode);
    }
} 
package com.btg.orders.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Order {
    Long id;
    Long orderCode;
    Long clientId;
    Client client;
    @Builder.Default
    List<OrderItem> items = new ArrayList<>();
    BigDecimal total;
    LocalDateTime createdAt;
    
    public Order(Long orderCode, Long clientId) {
        this.orderCode = orderCode;
        this.clientId = clientId;
        this.items = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.total = BigDecimal.ZERO;
    }
    
    public void addItem(OrderItem item) {
        this.items.add(item);
        calculateTotal();
    }
    
    public void removeItem(OrderItem item) {
        this.items.remove(item);
        calculateTotal();
    }
    
    public BigDecimal calculateTotal() {
        this.total = items.stream()
            .map(OrderItem::calculateTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        return this.total;
    }
    
    public void updateTotal() {
        items.forEach(OrderItem::updateTotal);
        calculateTotal();
    }
    
    public int getItemCount() {
        return items.size();
    }
    
    public boolean hasItems() {
        return !items.isEmpty();
    }
} 
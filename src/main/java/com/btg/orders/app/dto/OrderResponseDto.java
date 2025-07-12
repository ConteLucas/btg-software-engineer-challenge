package com.btg.orders.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class OrderResponseDto {
    Long id;
    Long orderCode;
    Long clientId;
    ClientResponseDto client;
    List<OrderItemResponseDto> items;
    BigDecimal total;
    LocalDateTime createdAt;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = PRIVATE)
    public static class OrderItemResponseDto {
        Long id;
        String product;
        Integer quantity;
        BigDecimal price;
        BigDecimal total;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = PRIVATE)
    public static class ClientResponseDto {
        Long id;
        String name;
        String email;
    }
} 
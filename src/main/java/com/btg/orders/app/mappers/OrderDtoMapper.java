package com.btg.orders.app.mappers;

import com.btg.orders.app.dto.OrderResponseDto;
import com.btg.orders.domain.entities.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderDtoMapper {
    OrderResponseDto toResponseDto(Order order);
    List<OrderResponseDto> toResponseDtoList(List<Order> orders);
} 
package com.btg.orders.infrastructure.database.mappers;

import com.btg.orders.domain.entities.OrderItem;
import com.btg.orders.infrastructure.database.models.OrderItemModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItem toDomain(OrderItemModel model);
    
    @Mapping(target = "order", ignore = true)
    OrderItemModel toModel(OrderItem domain);
    
    @Mapping(target = "order", ignore = true)
    void updateModel(OrderItem domain, @MappingTarget OrderItemModel model);
} 
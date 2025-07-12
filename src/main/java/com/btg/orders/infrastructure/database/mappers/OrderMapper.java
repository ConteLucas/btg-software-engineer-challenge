package com.btg.orders.infrastructure.database.mappers;

import com.btg.orders.domain.entities.Order;
import com.btg.orders.infrastructure.database.models.OrderModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, ClientMapper.class})
public interface OrderMapper {
    Order toDomain(OrderModel model);
    
    @Mapping(target = "client", ignore = true)
    OrderModel toModel(Order domain);
    
    @Mapping(target = "client", ignore = true)
    void updateModel(Order domain, @MappingTarget OrderModel model);
    
    @AfterMapping
    default void setOrderReference(@MappingTarget OrderModel orderModel) {
        if (orderModel.getItems() != null) {
            orderModel.getItems().forEach(item -> item.setOrder(orderModel));
        }
    }
} 
package com.btg.orders.infrastructure.database.mappers;

import com.btg.orders.domain.entities.Client;
import com.btg.orders.infrastructure.database.models.ClientModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    Client toDomain(ClientModel model);
    
    @Mapping(target = "orders", ignore = true)
    ClientModel toModel(Client domain);
    
    @Mapping(target = "orders", ignore = true)
    void updateModel(Client domain, @MappingTarget ClientModel model);
} 
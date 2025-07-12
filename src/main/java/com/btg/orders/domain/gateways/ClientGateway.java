package com.btg.orders.domain.gateways;

import com.btg.orders.domain.entities.Client;

import java.util.List;
import java.util.Optional;

public interface ClientGateway {
    
    Client save(Client client);
    
    Optional<Client> findById(Long id);
    
    Optional<Client> findByEmail(String email);
    
    List<Client> findAll();
    
    void deleteById(Long id);
    
    boolean existsById(Long id);
    
    Client findOrCreateDefaultClient(Long clientId);
} 
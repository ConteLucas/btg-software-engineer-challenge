package com.btg.orders.infrastructure.database.gateways;

import com.btg.orders.domain.entities.Client;
import com.btg.orders.domain.gateways.ClientGateway;
import com.btg.orders.infrastructure.database.mappers.ClientMapper;
import com.btg.orders.infrastructure.database.models.ClientModel;
import com.btg.orders.infrastructure.database.repositories.ClientJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClientDatabaseGateway implements ClientGateway {
    
    private final ClientJpaRepository repository;
    private final ClientMapper mapper;
    
    @Override
    public Client save(Client client) {
        log.info("Saving client: {}", client.getId());
        
        ClientModel model = mapper.toModel(client);
        ClientModel savedModel = repository.save(model);
        
        return mapper.toDomain(savedModel);
    }
    
    @Override
    public Optional<Client> findById(Long id) {
        log.info("Finding client by id: {}", id);
        
        return repository.findById(id)
            .map(mapper::toDomain);
    }
    
    @Override
    public Optional<Client> findByEmail(String email) {
        log.info("Finding client by email: {}", email);
        
        return repository.findByEmail(email)
            .map(mapper::toDomain);
    }
    
    @Override
    public List<Client> findAll() {
        log.info("Finding all clients");
        
        return repository.findAll().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        log.info("Deleting client by id: {}", id);
        
        repository.deleteById(id);
    }
    
    @Override
    public boolean existsById(Long id) {
        log.info("Checking if client exists by id: {}", id);
        
        return repository.existsById(id);
    }
    
    @Override
    public Client findOrCreateDefaultClient(Long clientId) {
        log.info("Finding or creating default client for id: {}", clientId);
        
        Optional<Client> existingClient = findById(clientId);
        if (existingClient.isPresent()) {
            return existingClient.get();
        }
        
        // Create default client
        Client defaultClient = Client.builder()
            .id(clientId)
            .name("Client " + clientId)
            .email("client" + clientId + "@example.com")
            .createdAt(java.time.LocalDateTime.now())
            .build();
        
        return save(defaultClient);
    }
} 
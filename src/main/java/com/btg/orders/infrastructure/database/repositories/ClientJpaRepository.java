package com.btg.orders.infrastructure.database.repositories;

import com.btg.orders.infrastructure.database.models.ClientModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientJpaRepository extends JpaRepository<ClientModel, Long> {
    
    Optional<ClientModel> findByEmail(String email);
    
    boolean existsByEmail(String email);
} 
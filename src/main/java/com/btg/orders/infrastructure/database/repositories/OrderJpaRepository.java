package com.btg.orders.infrastructure.database.repositories;

import com.btg.orders.infrastructure.database.models.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderModel, Long> {
    
    Optional<OrderModel> findByOrderCode(Long orderCode);
    
    List<OrderModel> findByClientId(Long clientId);
    
    @Query("SELECT o.total FROM OrderModel o WHERE o.orderCode = :orderCode")
    Optional<BigDecimal> findTotalByOrderCode(@Param("orderCode") Long orderCode);
    
    @Query("SELECT COUNT(o) FROM OrderModel o WHERE o.clientId = :clientId")
    Long countByClientId(@Param("clientId") Long clientId);
    
    boolean existsByOrderCode(Long orderCode);
} 
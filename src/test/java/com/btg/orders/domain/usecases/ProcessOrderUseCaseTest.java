package com.btg.orders.domain.usecases;

import com.btg.orders.domain.entities.Client;
import com.btg.orders.domain.entities.Order;
import com.btg.orders.domain.gateways.ClientGateway;
import com.btg.orders.domain.gateways.MessageGateway;
import com.btg.orders.domain.gateways.OrderGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessOrderUseCaseTest {
    
    @Mock
    private OrderGateway orderGateway;
    
    @Mock
    private ClientGateway clientGateway;
    
    @Mock
    private MessageGateway messageGateway;
    
    private ProcessOrderUseCase useCase;
    
    @BeforeEach
    void setUp() {
        useCase = new ProcessOrderUseCase(orderGateway, clientGateway, messageGateway);
    }
    
    @Test
    void shouldProcessOrderSuccessfully() {
        // Arrange
        Long orderCode = 1001L;
        Long clientId = 1L;
        List<ProcessOrderUseCase.OrderItemData> items = Arrays.asList(
            ProcessOrderUseCase.OrderItemData.builder()
                .product("Notebook")
                .quantity(1)
                .price(new BigDecimal("1000.00"))
                .build(),
            ProcessOrderUseCase.OrderItemData.builder()
                .product("Mouse")
                .quantity(2)
                .price(new BigDecimal("50.00"))
                .build()
        );
        
        Client client = new Client(clientId, "Test Client", "test@example.com");
        Order savedOrder = new Order(orderCode, clientId);
        savedOrder.setId(1L);
        
        when(orderGateway.existsByOrderCode(orderCode)).thenReturn(false);
        when(clientGateway.findOrCreateDefaultClient(clientId)).thenReturn(client);
        when(orderGateway.save(any(Order.class))).thenReturn(savedOrder);
        
        // Act
        Order result = useCase.execute(orderCode, clientId, items);
        
        // Assert
        assertNotNull(result);
        assertEquals(orderCode, result.getOrderCode());
        assertEquals(clientId, result.getClientId());
        
        verify(orderGateway).existsByOrderCode(orderCode);
        verify(clientGateway).findOrCreateDefaultClient(clientId);
        verify(orderGateway).save(any(Order.class));
        verify(messageGateway).sendOrderProcessedNotification(orderCode);
    }
    
    @Test
    void shouldThrowExceptionWhenOrderAlreadyExists() {
        // Arrange
        Long orderCode = 1001L;
        Long clientId = 1L;
        List<ProcessOrderUseCase.OrderItemData> items = Arrays.asList(
            ProcessOrderUseCase.OrderItemData.builder()
                .product("Notebook")
                .quantity(1)
                .price(new BigDecimal("1000.00"))
                .build()
        );
        
        when(orderGateway.existsByOrderCode(orderCode)).thenReturn(true);
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> useCase.execute(orderCode, clientId, items)
        );
        
        assertEquals("Order with code 1001 already exists", exception.getMessage());
        verify(orderGateway).existsByOrderCode(orderCode);
        verify(clientGateway, never()).findOrCreateDefaultClient(anyLong());
        verify(orderGateway, never()).save(any(Order.class));
        verify(messageGateway).sendOrderErrorNotification(eq(orderCode), anyString());
    }
    
    @Test
    void shouldSendErrorNotificationWhenProcessingFails() {
        // Arrange
        Long orderCode = 1001L;
        Long clientId = 1L;
        List<ProcessOrderUseCase.OrderItemData> items = Arrays.asList(
            ProcessOrderUseCase.OrderItemData.builder()
                .product("Notebook")
                .quantity(1)
                .price(new BigDecimal("1000.00"))
                .build()
        );
        
        when(orderGateway.existsByOrderCode(orderCode)).thenReturn(false);
        when(clientGateway.findOrCreateDefaultClient(clientId)).thenThrow(new RuntimeException("Database error"));
        
        // Act & Assert
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> useCase.execute(orderCode, clientId, items)
        );
        
        assertEquals("Database error", exception.getMessage());
        verify(messageGateway).sendOrderErrorNotification(eq(orderCode), eq("Database error"));
    }
    
    @Test
    void shouldCalculateCorrectTotal() {
        // Arrange
        Long orderCode = 1001L;
        Long clientId = 1L;
        List<ProcessOrderUseCase.OrderItemData> items = Arrays.asList(
            ProcessOrderUseCase.OrderItemData.builder()
                .product("Notebook")
                .quantity(1)
                .price(new BigDecimal("1000.00"))
                .build(),
            ProcessOrderUseCase.OrderItemData.builder()
                .product("Mouse")
                .quantity(2)
                .price(new BigDecimal("50.00"))
                .build()
        );
        
        Client client = Client.builder()
            .id(clientId)
            .name("Test Client")
            .email("test@example.com")
            .build();
        
        when(orderGateway.existsByOrderCode(orderCode)).thenReturn(false);
        when(clientGateway.findOrCreateDefaultClient(clientId)).thenReturn(client);
        when(orderGateway.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1L);
            return order;
        });
        
        // Act
        Order result = useCase.execute(orderCode, clientId, items);
        
        // Assert
        assertEquals(new BigDecimal("1100.00"), result.getTotal());
        assertEquals(2, result.getItems().size());
    }
} 
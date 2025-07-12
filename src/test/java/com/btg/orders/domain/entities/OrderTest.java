package com.btg.orders.domain.entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    
    private Order order;
    
    @BeforeEach
    void setUp() {
        order = new Order(1001L, 1L);
    }
    
    @Test
    void shouldCreateOrderWithCorrectValues() {
        assertEquals(1001L, order.getOrderCode());
        assertEquals(1L, order.getClientId());
        assertEquals(BigDecimal.ZERO, order.getTotal());
        assertNotNull(order.getCreatedAt());
        assertTrue(order.getItems().isEmpty());
    }
    
    @Test
    void shouldAddItemAndCalculateTotal() {
        OrderItem item = OrderItem.builder()
            .product("Notebook")
            .quantity(1)
            .price(new BigDecimal("1000.00"))
            .build();
        item.updateTotal();
        
        order.addItem(item);
        
        assertEquals(1, order.getItems().size());
        assertEquals(new BigDecimal("1000.00"), order.getTotal());
        assertTrue(order.hasItems());
    }
    
    @Test
    void shouldAddMultipleItemsAndCalculateTotal() {
        OrderItem item1 = OrderItem.builder()
            .product("Notebook")
            .quantity(1)
            .price(new BigDecimal("1000.00"))
            .build();
        item1.updateTotal();
            
        OrderItem item2 = OrderItem.builder()
            .product("Mouse")
            .quantity(2)
            .price(new BigDecimal("50.00"))
            .build();
        item2.updateTotal();
        
        order.addItem(item1);
        order.addItem(item2);
        
        assertEquals(2, order.getItems().size());
        assertEquals(new BigDecimal("1100.00"), order.getTotal());
    }
    
    @Test
    void shouldRemoveItemAndRecalculateTotal() {
        OrderItem item1 = OrderItem.builder()
            .product("Notebook")
            .quantity(1)
            .price(new BigDecimal("1000.00"))
            .build();
        item1.updateTotal();
            
        OrderItem item2 = OrderItem.builder()
            .product("Mouse")
            .quantity(2)
            .price(new BigDecimal("50.00"))
            .build();
        item2.updateTotal();
        
        order.addItem(item1);
        order.addItem(item2);
        order.removeItem(item1);
        
        assertEquals(1, order.getItems().size());
        assertEquals(new BigDecimal("100.00"), order.getTotal());
    }
    
    @Test
    void shouldUpdateTotalWhenItemsChange() {
        OrderItem item = OrderItem.builder()
            .product("Notebook")
            .quantity(1)
            .price(new BigDecimal("1000.00"))
            .build();
        item.updateTotal();
        order.addItem(item);
        
        // Modify item
        item.setQuantity(2);
        order.updateTotal();
        
        assertEquals(new BigDecimal("2000.00"), order.getTotal());
    }
    
    @Test
    void shouldReturnCorrectItemCount() {
        assertEquals(0, order.getItemCount());
        
        OrderItem item1 = OrderItem.builder()
            .product("Item1")
            .quantity(1)
            .price(new BigDecimal("10.00"))
            .build();
        item1.updateTotal();
        order.addItem(item1);
        assertEquals(1, order.getItemCount());
        
        OrderItem item2 = OrderItem.builder()
            .product("Item2")
            .quantity(1)
            .price(new BigDecimal("20.00"))
            .build();
        item2.updateTotal();
        order.addItem(item2);
        assertEquals(2, order.getItemCount());
    }
    
    @Test
    void shouldReturnFalseWhenNoItems() {
        assertFalse(order.hasItems());
        
        OrderItem item = OrderItem.builder()
            .product("Item1")
            .quantity(1)
            .price(new BigDecimal("10.00"))
            .build();
        item.updateTotal();
        order.addItem(item);
        assertTrue(order.hasItems());
    }
} 
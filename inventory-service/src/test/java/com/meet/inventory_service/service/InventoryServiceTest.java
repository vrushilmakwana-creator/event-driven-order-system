package com.meet.inventory_service.service;

import com.meet.inventory_service.event.OrderCreatedEvent;
import com.meet.inventory_service.model.InventoryItem;
import com.meet.inventory_service.model.InventoryReservation;
import com.meet.inventory_service.repository.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    void reserveStockReservesWhenEnoughStockIsAvailable() {
        InventoryItem item = new InventoryItem("P101", 100);
        when(inventoryRepository.findById("P101")).thenReturn(Optional.of(item));
        when(inventoryRepository.save(item)).thenReturn(item);
        OrderCreatedEvent event = new OrderCreatedEvent("ORDER-1", "U101", "P101", 2, 200.0);

        InventoryReservation reservation = inventoryService.reserveStock(event);

        assertThat(reservation.isReserved()).isTrue();
        assertThat(reservation.getRemainingQuantity()).isEqualTo(98);
        assertThat(item.getAvailableQuantity()).isEqualTo(98);
        verify(inventoryRepository).save(item);
    }

    @Test
    void reserveStockFailsWhenStockIsInsufficient() {
        when(inventoryRepository.findById("P103")).thenReturn(Optional.of(new InventoryItem("P103", 30)));
        OrderCreatedEvent event = new OrderCreatedEvent("ORDER-1", "U101", "P103", 100, 10000.0);

        InventoryReservation reservation = inventoryService.reserveStock(event);

        assertThat(reservation.isReserved()).isFalse();
        assertThat(reservation.getAvailableQuantity()).isEqualTo(30);
        assertThat(reservation.getFailureReason()).isEqualTo("Insufficient stock");
    }

    @Test
    void reserveStockFailsWhenProductDoesNotExist() {
        when(inventoryRepository.findById("MISSING")).thenReturn(Optional.empty());
        OrderCreatedEvent event = new OrderCreatedEvent("ORDER-1", "U101", "MISSING", 4, 400.0);

        InventoryReservation reservation = inventoryService.reserveStock(event);

        assertThat(reservation.isReserved()).isFalse();
        assertThat(reservation.getFailureReason()).isEqualTo("Product not found");
    }
}

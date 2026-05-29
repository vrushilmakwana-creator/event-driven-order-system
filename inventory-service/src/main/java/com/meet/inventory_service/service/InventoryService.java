package com.meet.inventory_service.service;

import com.meet.inventory_service.event.OrderCreatedEvent;
import com.meet.inventory_service.model.InventoryItem;
import com.meet.inventory_service.model.InventoryReservation;
import com.meet.inventory_service.repository.InventoryRepository;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public InventoryReservation reserveStock(OrderCreatedEvent event) {
        if (event.getQuantity() <= 0) {
            return new InventoryReservation(false, event.getQuantity(), 0, 0, "Quantity must be positive");
        }

        InventoryItem item = inventoryRepository.findById(event.getProductId()).orElse(null);
        if (item == null) {
            return new InventoryReservation(false, event.getQuantity(), 0, 0, "Product not found");
        }

        int availableQuantity = item.getAvailableQuantity();
        if (availableQuantity < event.getQuantity()) {
            return new InventoryReservation(
                    false,
                    event.getQuantity(),
                    availableQuantity,
                    availableQuantity,
                    "Insufficient stock"
            );
        }

        int remainingQuantity = availableQuantity - event.getQuantity();
        item.setAvailableQuantity(remainingQuantity);
        inventoryRepository.save(item);

        return new InventoryReservation(true, event.getQuantity(), availableQuantity, remainingQuantity, null);
    }
}

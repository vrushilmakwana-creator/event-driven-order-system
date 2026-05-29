package com.meet.inventory_service.config;

import com.meet.inventory_service.model.InventoryItem;
import com.meet.inventory_service.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InventoryDataSeeder implements CommandLineRunner {

    private final InventoryRepository inventoryRepository;

    public InventoryDataSeeder(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public void run(String... args) {
        List<InventoryItem> defaultItems = List.of(
                new InventoryItem("P101", 100),
                new InventoryItem("P102", 50),
                new InventoryItem("P103", 30),
                new InventoryItem("M007", 100)
        );

        defaultItems.stream()
                .filter(item -> !inventoryRepository.existsById(item.getProductId()))
                .forEach(inventoryRepository::save);
    }
}

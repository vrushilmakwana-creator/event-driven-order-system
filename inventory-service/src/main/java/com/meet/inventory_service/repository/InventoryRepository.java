package com.meet.inventory_service.repository;

import com.meet.inventory_service.model.InventoryItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InventoryRepository extends MongoRepository<InventoryItem, String> {
}

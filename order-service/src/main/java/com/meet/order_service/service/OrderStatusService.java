package com.meet.order_service.service;

import com.meet.order_service.model.Order;
import com.meet.order_service.model.OrderStatus;
import com.meet.order_service.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderStatusService {

    private static final Logger log = LoggerFactory.getLogger(OrderStatusService.class);

    private final OrderRepository orderRepository;

    public OrderStatusService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Optional<Order> updateStatus(String orderId, OrderStatus status) {
        Optional<Order> existingOrder = orderRepository.findById(orderId);
        if (existingOrder.isEmpty()) {
            log.warn("Skipping status update because order was not found: orderId={}, status={}", orderId, status);
            return Optional.empty();
        }

        Order order = existingOrder.get();
        order.setStatus(status);
        return Optional.of(orderRepository.save(order));
    }
}

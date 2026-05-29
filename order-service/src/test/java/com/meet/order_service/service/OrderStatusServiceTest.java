package com.meet.order_service.service;

import com.meet.order_service.model.Order;
import com.meet.order_service.model.OrderStatus;
import com.meet.order_service.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderStatusServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderStatusService orderStatusService;

    @Test
    void updateStatusUpdatesExistingOrder() {
        Order order = new Order();
        order.setId("ORDER-1");
        order.setStatus(OrderStatus.CREATED);

        when(orderRepository.findById("ORDER-1")).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        Optional<Order> updatedOrder = orderStatusService.updateStatus("ORDER-1", OrderStatus.CANCELLED);

        assertThat(updatedOrder).contains(order);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
        verify(orderRepository).save(order);
    }

    @Test
    void updateStatusSkipsMissingOrder() {
        when(orderRepository.findById("MISSING-ORDER")).thenReturn(Optional.empty());

        Optional<Order> updatedOrder = orderStatusService.updateStatus("MISSING-ORDER", OrderStatus.CANCELLED);

        assertThat(updatedOrder).isEmpty();
        verify(orderRepository, never()).save(org.mockito.ArgumentMatchers.any(Order.class));
    }
}

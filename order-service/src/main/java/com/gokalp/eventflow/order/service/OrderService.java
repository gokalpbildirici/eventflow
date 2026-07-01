package com.gokalp.eventflow.order.service;

import java.util.Collection;
import java.util.function.Supplier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import com.gokalp.eventflow.common.events.OrderCreatedEvent;
import com.gokalp.eventflow.order.model.Order;
import com.gokalp.eventflow.order.repository.OrderRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final Sinks.Many<OrderCreatedEvent> orderSink = Sinks.many().unicast().onBackpressureBuffer();

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(String customerId, String productId, int quantity, java.math.BigDecimal totalAmount) {
        Order order = new Order(customerId, productId, quantity, totalAmount);
        orderRepository.save(order);

        OrderCreatedEvent event = new OrderCreatedEvent(
            order.getId(), customerId, productId, quantity, totalAmount);
        orderSink.tryEmitNext(event);

        return order;
    }

    public Order getOrder(String id) {
        return orderRepository.findById(id);
    }

    public Collection<Order> getAllOrders() {
        return orderRepository.findAll().values();
    }

    @Bean
    public Supplier<Flux<OrderCreatedEvent>> orderCreatedSupplier() {
        return orderSink::asFlux;
    }
}

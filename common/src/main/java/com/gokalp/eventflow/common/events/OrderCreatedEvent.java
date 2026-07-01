package com.gokalp.eventflow.common.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderCreatedEvent(
    UUID eventId,
    String orderId,
    String customerId,
    String productId,
    int quantity,
    BigDecimal totalAmount,
    Instant timestamp
) {
    public OrderCreatedEvent(String orderId, String customerId, String productId, int quantity, BigDecimal totalAmount) {
        this(UUID.randomUUID(), orderId, customerId, productId, quantity, totalAmount, Instant.now());
    }
}

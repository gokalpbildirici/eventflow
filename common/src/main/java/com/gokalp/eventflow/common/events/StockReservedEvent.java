package com.gokalp.eventflow.common.events;

import java.time.Instant;
import java.util.UUID;

public record StockReservedEvent(
    UUID eventId,
    String productId,
    String orderId,
    int quantityReserved,
    int remainingStock,
    StockStatus status,
    Instant timestamp
) {
    public StockReservedEvent(String productId, String orderId, int quantityReserved, int remainingStock, StockStatus status) {
        this(UUID.randomUUID(), productId, orderId, quantityReserved, remainingStock, status, Instant.now());
    }
}

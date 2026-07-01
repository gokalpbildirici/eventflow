package com.gokalp.eventflow.common.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentProcessedEvent(
    UUID eventId,
    String paymentId,
    String orderId,
    String customerId,
    BigDecimal amount,
    PaymentStatus status,
    Instant timestamp
) {
    public PaymentProcessedEvent(String paymentId, String orderId, String customerId, BigDecimal amount, PaymentStatus status) {
        this(UUID.randomUUID(), paymentId, orderId, customerId, amount, status, Instant.now());
    }
}

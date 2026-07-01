package com.gokalp.eventflow.payment.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import com.gokalp.eventflow.common.events.PaymentStatus;

public class Payment {

    private String id;
    private String orderId;
    private String customerId;
    private BigDecimal amount;
    private PaymentStatus status;
    private Instant createdAt;

    public Payment() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
    }

    public Payment(String orderId, String customerId, BigDecimal amount, PaymentStatus status) {
        this();
        this.orderId = orderId;
        this.customerId = customerId;
        this.amount = amount;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}

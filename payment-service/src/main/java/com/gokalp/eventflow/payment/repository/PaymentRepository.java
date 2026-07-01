package com.gokalp.eventflow.payment.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;
import com.gokalp.eventflow.payment.model.Payment;

@Repository
public class PaymentRepository {

    private final Map<String, Payment> payments = new ConcurrentHashMap<>();

    public Payment save(Payment payment) {
        payments.put(payment.getId(), payment);
        return payment;
    }

    public Payment findByOrderId(String orderId) {
        return payments.values().stream()
            .filter(p -> p.getOrderId().equals(orderId))
            .findFirst().orElse(null);
    }
}

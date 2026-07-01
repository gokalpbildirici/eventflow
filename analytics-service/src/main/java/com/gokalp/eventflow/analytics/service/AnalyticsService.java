package com.gokalp.eventflow.analytics.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import com.gokalp.eventflow.common.events.OrderCreatedEvent;
import com.gokalp.eventflow.common.events.PaymentProcessedEvent;
import com.gokalp.eventflow.common.events.PaymentStatus;
import com.gokalp.eventflow.common.events.StockReservedEvent;
import com.gokalp.eventflow.common.events.StockStatus;

@Service
public class AnalyticsService {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsService.class);

    private final AtomicInteger totalOrders = new AtomicInteger(0);
    private final AtomicInteger successfulPayments = new AtomicInteger(0);
    private final AtomicInteger failedPayments = new AtomicInteger(0);
    private final AtomicInteger stockReservations = new AtomicInteger(0);
    private final AtomicInteger stockFailures = new AtomicInteger(0);
    private final Map<String, AtomicInteger> productSales = new ConcurrentHashMap<>();

    public Map<String, Object> getSummary() {
        return Map.of(
            "totalOrders", totalOrders.get(),
            "successfulPayments", successfulPayments.get(),
            "failedPayments", failedPayments.get(),
            "stockReservations", stockReservations.get(),
            "stockFailures", stockFailures.get(),
            "productSales", productSales.entrySet().stream()
                .map(e -> Map.of("productId", e.getKey(), "count", e.getValue().get()))
                .toList()
        );
    }

    @Bean
    public Consumer<OrderCreatedEvent> orderAnalytics() {
        return event -> {
            totalOrders.incrementAndGet();
            productSales.computeIfAbsent(event.productId(), k -> new AtomicInteger(0))
                .addAndGet(event.quantity());
            log.info("ANALYTICS [Order] Total orders: {}", totalOrders.get());
        };
    }

    @Bean
    public Consumer<PaymentProcessedEvent> paymentAnalytics() {
        return event -> {
            if (event.status() == PaymentStatus.COMPLETED) {
                successfulPayments.incrementAndGet();
            } else {
                failedPayments.incrementAndGet();
            }
            log.info("ANALYTICS [Payment] Success: {}, Failed: {}", successfulPayments.get(), failedPayments.get());
        };
    }

    @Bean
    public Consumer<StockReservedEvent> stockAnalytics() {
        return event -> {
            if (event.status() == StockStatus.RESERVED) {
                stockReservations.incrementAndGet();
            } else {
                stockFailures.incrementAndGet();
            }
            log.info("ANALYTICS [Stock] Reserved: {}, Failed: {}", stockReservations.get(), stockFailures.get());
        };
    }
}

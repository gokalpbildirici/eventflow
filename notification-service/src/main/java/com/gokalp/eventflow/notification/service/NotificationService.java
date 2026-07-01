package com.gokalp.eventflow.notification.service;

import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import com.gokalp.eventflow.common.events.OrderCreatedEvent;
import com.gokalp.eventflow.common.events.PaymentProcessedEvent;
import com.gokalp.eventflow.common.events.StockReservedEvent;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Bean
    public Consumer<OrderCreatedEvent> orderCreatedNotification() {
        return event -> log.info("NOTIFICATION [Order Created] Order {} by customer {} | Product: {} x {} | Total: ${}",
            event.orderId(), event.customerId(), event.productId(), event.quantity(), event.totalAmount());
    }

    @Bean
    public Consumer<PaymentProcessedEvent> paymentProcessedNotification() {
        return event -> log.info("NOTIFICATION [Payment {}] Payment {} for order {} | Customer: {} | Amount: ${}",
            event.status(), event.paymentId(), event.orderId(), event.customerId(), event.amount());
    }

    @Bean
    public Consumer<StockReservedEvent> stockReservedNotification() {
        return event -> log.info("NOTIFICATION [Stock {}] Product {} | Order {} | Reserved: {} | Remaining: {}",
            event.status(), event.productId(), event.orderId(), event.quantityReserved(), event.remainingStock());
    }
}

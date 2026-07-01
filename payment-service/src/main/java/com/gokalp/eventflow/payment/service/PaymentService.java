package com.gokalp.eventflow.payment.service;

import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import com.gokalp.eventflow.common.events.OrderCreatedEvent;
import com.gokalp.eventflow.common.events.PaymentProcessedEvent;
import com.gokalp.eventflow.common.events.PaymentStatus;
import com.gokalp.eventflow.payment.model.Payment;
import com.gokalp.eventflow.payment.repository.PaymentRepository;
import reactor.core.publisher.Flux;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Bean
    public Function<Flux<OrderCreatedEvent>, Flux<PaymentProcessedEvent>> paymentProcessor() {
        return orderEvents -> orderEvents.map(this::processPayment);
    }

    private PaymentProcessedEvent processPayment(OrderCreatedEvent event) {
        log.info("Processing payment for order: {}", event.orderId());
        PaymentStatus status = Math.random() > 0.2 ? PaymentStatus.COMPLETED : PaymentStatus.FAILED;
        Payment payment = new Payment(event.orderId(), event.customerId(), event.totalAmount(), status);
        paymentRepository.save(payment);
        log.info("Payment {} for order {}: {}", payment.getId(), event.orderId(), status);
        return new PaymentProcessedEvent(payment.getId(), event.orderId(), event.customerId(), event.totalAmount(), status);
    }
}

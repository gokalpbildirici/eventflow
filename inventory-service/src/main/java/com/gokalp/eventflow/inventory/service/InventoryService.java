package com.gokalp.eventflow.inventory.service;

import java.util.Collection;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import com.gokalp.eventflow.common.events.OrderCreatedEvent;
import com.gokalp.eventflow.common.events.StockReservedEvent;
import com.gokalp.eventflow.common.events.StockStatus;
import com.gokalp.eventflow.inventory.model.Product;
import com.gokalp.eventflow.inventory.repository.ProductRepository;
import reactor.core.publisher.Flux;

@Service
public class InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);
    private final ProductRepository productRepository;

    public InventoryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
        seedProducts();
    }

    private void seedProducts() {
        productRepository.save(new Product("P001", "Laptop", 50, new java.math.BigDecimal("1500.00")));
        productRepository.save(new Product("P002", "Mouse", 100, new java.math.BigDecimal("25.00")));
        productRepository.save(new Product("P003", "Keyboard", 75, new java.math.BigDecimal("75.00")));
        productRepository.save(new Product("P004", "Monitor", 30, new java.math.BigDecimal("300.00")));
    }

    @Bean
    public Function<Flux<OrderCreatedEvent>, Flux<StockReservedEvent>> stockReserver() {
        return orderEvents -> orderEvents.map(this::reserveStock);
    }

    private StockReservedEvent reserveStock(OrderCreatedEvent event) {
        log.info("Reserving stock for product: {} (order: {})", event.productId(), event.orderId());
        Product product = productRepository.findById(event.productId());
        if (product != null && product.getStock() >= event.quantity()) {
            product.setStock(product.getStock() - event.quantity());
            log.info("Stock reserved for product {}. Remaining: {}", product.getId(), product.getStock());
            return new StockReservedEvent(event.productId(), event.orderId(), event.quantity(), product.getStock(), StockStatus.RESERVED);
        }
        log.warn("Insufficient stock for product: {}", event.productId());
        int remaining = product != null ? product.getStock() : 0;
        return new StockReservedEvent(event.productId(), event.orderId(), event.quantity(), remaining, StockStatus.INSUFFICIENT_STOCK);
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Product getProduct(String id) {
        return productRepository.findById(id);
    }

    public Collection<Product> getAllProducts() {
        return productRepository.findAll().values();
    }
}

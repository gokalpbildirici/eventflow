package com.gokalp.eventflow.inventory.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;
import com.gokalp.eventflow.inventory.model.Product;

@Repository
public class ProductRepository {

    private final Map<String, Product> products = new ConcurrentHashMap<>();

    public Product save(Product product) {
        products.put(product.getId(), product);
        return product;
    }

    public Product findById(String id) {
        return products.get(id);
    }

    public Map<String, Product> findAll() {
        return Map.copyOf(products);
    }
}

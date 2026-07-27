package com.ali.crud.example.service;

import com.ali.crud.example.config.CacheMetricsInterceptor;
import com.ali.crud.example.entity.Product;
import com.ali.crud.example.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductService {
    @Autowired
    private ProductRepository repository;

    @Autowired
    private CacheMetricsInterceptor cacheMetrics;

    @Autowired
    private CacheManager cacheManager;

    @CacheEvict(value = "products", allEntries = true)
    public Product saveProduct(Product product) {
        log.info("Saving product: {}", product.getName());
        log.debug("Cache evicted for all products");
        return repository.save(product);
    }

    @CacheEvict(value = "products", allEntries = true)
    public List<Product> saveProducts(List<Product> products) {
        log.info("Saving {} products", products.size());
        log.debug("Cache evicted for all products");
        return repository.saveAll(products);
    }

    @Cacheable(value = "products", key = "'allProducts'")
    public List<Product> getProducts() {
        if (isCached("products", "'allProducts'")) {
            cacheMetrics.recordHit();
        } else {
            cacheMetrics.recordMiss();
            log.warn("❌ CACHE MISS - Fetching all products from DATABASE");
        }
        return repository.findAll();
    }

    @Cacheable(value = "products", key = "#id")
    public Product getProductById(int id) {
        if (isCached("products", id)) {
            cacheMetrics.recordHit();
        } else {
            cacheMetrics.recordMiss();
            log.warn("❌ CACHE MISS for ID: {} - Fetching from DATABASE", id);
        }
        return repository.findById(id).orElse(null);
    }

    @Cacheable(value = "products", key = "#name")
    public Product getProductByName(String name) {
        if (isCached("products", name)) {
            cacheMetrics.recordHit();
        } else {
            cacheMetrics.recordMiss();
            log.warn("❌ CACHE MISS for Name: {} - Fetching from DATABASE", name);
        }
        return repository.findByName(name);
    }

    @CacheEvict(value = "products", allEntries = true)
    public String deleteProduct(int id) {
        log.info("Deleting product with ID: {}", id);
        log.debug("Cache evicted for all products");
        repository.deleteById(id);
        return "product removed !! " + id;
    }

    @CacheEvict(value = "products", allEntries = true)
    public Product updateProduct(Product product) {
        log.info("Updating product ID: {} to name: {}", product.getId(), product.getName());
        log.debug("Cache evicted for all products");
        Product existingProduct = repository.findById(product.getId()).orElse(null);
        existingProduct.setName(product.getName());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setPrice(product.getPrice());
        return repository.save(existingProduct);
    }

    private boolean isCached(String cacheName, Object key) {
        try {
            var cache = cacheManager.getCache(cacheName);
            return cache != null && cache.get(key) != null;
        } catch (Exception e) {
            return false;
        }
    }

}

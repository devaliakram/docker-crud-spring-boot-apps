package com.ali.crud.example.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
public class CacheMetricsInterceptor {
    private final AtomicLong cacheHits = new AtomicLong(0);
    private final AtomicLong cacheMisses = new AtomicLong(0);

    public void recordHit() {
        log.info("✅ CACHE HIT - Total Hits: {}", cacheHits.incrementAndGet());
    }

    public void recordMiss() {
        log.info("❌ CACHE MISS - Total Misses: {}", cacheMisses.incrementAndGet());
    }

    public void printStats() {
        log.info("Cache Stats - Hits: {}, Misses: {}, Hit Rate: {}%",
                cacheHits.get(),
                cacheMisses.get(),
                cacheMisses.get() == 0 ? 100 :
                        (cacheHits.get() * 100) / (cacheHits.get() + cacheMisses.get())
        );
    }
}
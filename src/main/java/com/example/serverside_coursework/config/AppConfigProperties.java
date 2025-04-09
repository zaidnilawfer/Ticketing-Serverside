package com.example.serverside_coursework.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfigProperties {
    @Value("${ticket.pool.capacity:1000}")
    private int poolCapacity;

    @Value("${vendor.thread.pool.size:5}")
    private int threadPoolSize;

    @Value("${vendor.batch.size:10}")
    private int batchSize;

    // Getters
    public int getPoolCapacity() {
        return poolCapacity;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public int getBatchSize() {
        return batchSize;
    }
}
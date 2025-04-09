package com.example.serverside_coursework.config;

import com.example.serverside_coursework.concurrency.TicketPool;
import com.example.serverside_coursework.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TicketConfig {
    @Autowired
    private AppConfigProperties configProperties;

    @Bean
    public TicketPool ticketPool(TicketService ticketService) {
        return new TicketPool(configProperties.getPoolCapacity(), ticketService);
    }

    @Bean
    public int threadPoolSize() {
        return configProperties.getThreadPoolSize();
    }

    @Bean
    public int batchSize() {
        return configProperties.getBatchSize();
    }
}
package com.example.serverside_coursework.config;

import com.example.serverside_coursework.concurrency.TicketWebSocket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Bean
    public TicketWebSocket ticketWebSocket() {
        return new TicketWebSocket();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(ticketWebSocket(), "/ws/tickets")
                .setAllowedOrigins("*");
    }
}
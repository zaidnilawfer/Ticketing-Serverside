package com.example.serverside_coursework.concurrency;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TicketWebSocket extends TextWebSocketHandler {
    private static final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        sessions.remove(session);
    }

    public static void broadcastTicketCount(int count) {
        TextMessage message = new TextMessage(String.valueOf(count));
        sessions.forEach(session -> {
            try {
                if (session.isOpen()) {
                    session.sendMessage(message);
                }
            } catch (Exception e) {
                sessions.remove(session);
            }
        });
    }
}
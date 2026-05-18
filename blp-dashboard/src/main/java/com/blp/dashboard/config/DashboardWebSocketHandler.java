package com.blp.dashboard.config;

import cn.hutool.json.JSONUtil;
import com.blp.dashboard.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DashboardWebSocketHandler extends TextWebSocketHandler {

    private static final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final DashboardService dashboardService;

    public DashboardWebSocketHandler(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.put(session.getId(), session);
        log.info("WebSocket connected: {}", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session.getId());
        log.info("WebSocket disconnected: {}", session.getId());
    }

    @Scheduled(fixedDelay = 5000)
    public void pushStats() {
        if (sessions.isEmpty()) {
            return;
        }
        try {
            String json = JSONUtil.toJsonStr(dashboardService.getStats());
            TextMessage message = new TextMessage(json);
            for (WebSocketSession session : sessions.values()) {
                if (session.isOpen()) {
                    try {
                        session.sendMessage(message);
                    } catch (IOException e) {
                        log.error("Failed to send WebSocket message to {}", session.getId(), e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to push dashboard stats", e);
        }
    }
}

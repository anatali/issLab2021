package it.unibo.wsdemoNoSTOMP;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/*
In Spring we can create a customized handler by extends abstract class
AbstractWebSocketHandler or one of it's subclass,
either TextWebSocketHandler or BinaryWebSocketHandler:
 */
public class WebSocketHandler extends AbstractWebSocketHandler {
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("Added the session:" + session);
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        System.out.println("Removed the session:" + session);
        super.afterConnectionClosed(session, status);
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        System.out.println("New Text Message Received");
        session.sendMessage(message);
    }
    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws IOException {
        System.out.println("New Binary Message Received");
        session.sendMessage(message);
    }
}

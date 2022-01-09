package it.unibo.wsdemoSTOMP;

import org.springframework.web.socket.*;
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
        String payload = message.getPayload();
        System.out.println("New Text Message Received " + message);
        System.out.println("Message getPayloadLength=" + (payload.startsWith("CONNECT")) );
            //session.sendMessage(message);
        if( payload.startsWith("SEND") ) {
            String msg = payload.substring(
                    payload.indexOf("{"),payload.lastIndexOf("}")+1);
            System.out.println("msg= " + msg);
            TextMessage  m = new TextMessage (msg);
            session.sendMessage(m);

        }
    }
    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws IOException {
        System.out.println("New Binary Message Received");
        session.sendMessage(message);
    }
}

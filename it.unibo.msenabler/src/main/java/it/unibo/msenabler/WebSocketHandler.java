package it.unibo.msenabler;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import it.unibo.enablerCleanArch.supports.Colors;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/*
In Spring we can create a customized handler by extends abstract class
AbstractWebSocketHandler or one of it's subclass,
either TextWebSocketHandler or BinaryWebSocketHandler:
 */
public class WebSocketHandler extends AbstractWebSocketHandler {
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private static WebSocketHandler handler = null;
    
    public static WebSocketHandler getWebSocketHandler() {
    	if(handler == null) { handler = new WebSocketHandler(); }
    	return handler;
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        Colors.outappl("WebSocketHandler | Added the session:" + session, Colors.ANSI_PURPLE);
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        Colors.outappl("WebSocketHandler | Removed the session:" + session, Colors.ANSI_PURPLE);
        super.afterConnectionClosed(session, status);
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        Colors.outappl("WebSocketHandler | New Text Message Received: " + message, Colors.ANSI_PURPLE);
         //Send to all the connected clients
        sendToAll(message);
    }
    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws IOException {
        Colors.outappl("WebSocketHandler | New Binary Message Received ", Colors.ANSI_PURPLE );
         //Send to all the connected clients
        Iterator<WebSocketSession> iter = sessions.iterator();
        while( iter.hasNext() ){
            iter.next().sendMessage(message);
        }
    }
    public void sendToAll(String message) throws IOException{
    	sendToAll( new TextMessage(message) );
     }

    protected void sendToAll(TextMessage message) throws IOException{
        Colors.outappl("WebSocketHandler | sendToAll " + sessions.size() + " " + message , Colors.ANSI_PURPLE );
        Iterator<WebSocketSession> iter = sessions.iterator();
        while( iter.hasNext() ){
            iter.next().sendMessage(message);
        }
    }

}
package it.unibo.msenabler.client;

import javax.imageio.ImageIO;
import javax.websocket.*;

import it.unibo.enablerCleanArch.supports.Utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;


@ClientEndpoint
public class SonarWsObserver {

    Session userSession = null;
    private IMessageHandler messageHandler;

    public SonarWsObserver(URI endpointURI) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Callback hook for Connection open events.
     *
     * @param userSession the userSession which is opened.
     */
    @OnOpen
    public void onOpen(Session userSession) {
        System.out.println("opening websocket");
        this.userSession = userSession;
    }

    /**
     * Callback hook for Connection close events.
     *
     * @param userSession the userSession which is getting closed.
     * @param reason the reason for connection close
     */
    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("closing websocket");
        this.userSession = null;
    }

    /**
     * Callback hook for Message Events. This method will be invoked when a client send a message.
     */
    @OnMessage
    public void onMessage(String message) {
        //System.out.println("onMessage message=" + message);
        if (this.messageHandler != null) {
            this.messageHandler.handleMessage(message);
        }
    }

    @OnMessage
    public void onMessage(ByteBuffer bytes) {
        System.out.println("Received ... " + bytes);
        System.out.println("Handle byte buffer");
        try{
             ByteArrayInputStream bis = new ByteArrayInputStream(bytes.array());
             BufferedImage bImage2 = ImageIO.read(bis);
            ImageIO.write(bImage2, "jpg", new File("outputimage.jpg") );
            System.out.println("image created");
        }catch( Exception e){
            throw new RuntimeException(e);
        }

    }


    /**
     * register message handler
      * @param msgHandler
     */
    public void addMessageHandler(IMessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }

    /**
     * Send a message.
     * @param message
     */
    public void sendMessage(String message) {
        System.out.println("Sending ... " + message);
        this.userSession.getAsyncRemote().sendText(message);
    }

    
    public static void main(String[] args) {
        try {
            // open websocket
            SonarWsObserver clientEndPoint =
                    new SonarWsObserver(new URI("ws://192.168.1.22:8081/sonarsocket"));

            // add listener
            clientEndPoint.addMessageHandler(new IMessageHandler() {
                public void handleMessage(String message) {
                    System.out.println(message);
                }
            });

            // send message to websocket
            //clientEndPoint.sendMessage("hello from Java client");


            // wait for messages from websocket
            Utils.delay(6000000);

        }  catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
        }
    }
 

}

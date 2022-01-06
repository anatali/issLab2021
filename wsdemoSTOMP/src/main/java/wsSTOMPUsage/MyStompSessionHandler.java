package wsSTOMPUsage;

import it.unibo.wsdemoSTOMP.InputMessage;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

public class MyStompSessionHandler extends StompSessionHandlerAdapter {
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println("New session established : " + session.getSessionId());
        session.subscribe("/topic/messages", this);
        System.out.println("Subscribed to /topic/messages");
        session.send("/app/chat", getSampleMessage());
        System.out.println("InputMessage sent to websocket server");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        //logger.error("Got an exception", exception);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return InputMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        InputMessage msg = (InputMessage) payload;
        System.out.println("Received : " + msg.getName()  );
    }

    /**
     * A sample InputMessage instance.
     * @return instance of <code>InputMessage</code>
     */
    private InputMessage getSampleMessage() {
        InputMessage msg = new InputMessage();
        msg.setName("Nicky");
        //msg.setText("Howdy!!");
        return msg;
    }
}

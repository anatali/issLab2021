package wsSTOMPUsage;

import it.unibo.wsdemoSTOMP.InputMessage;
import it.unibo.wsdemoSTOMP.OutputMessage;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

public class MyStompSessionHandler extends StompSessionHandlerAdapter {
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println("MyStompSessionHandler | New session established : " + session.getSessionId());
        session.subscribe("/topic/output", this);
        System.out.println("Subscribed to /topic/output");
        session.send("/unibo/inputmsg", getSampleMessage());
        System.out.println("MyStompSessionHandler | InputMessage sent to websocket server");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        //logger.error("Got an exception", exception);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return OutputMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        //System.out.println("MyStompSessionHandler | Received : " + payload + " " + payload.getClass() );
        if( payload instanceof OutputMessage) {
            OutputMessage msg = (OutputMessage) payload;
            System.out.println("MyStompSessionHandler | Received : " + msg.getContent());
        }
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

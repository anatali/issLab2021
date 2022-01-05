package wsusageInJava;
import java.net.URI;
import java.net.URISyntaxException;

public class TestClient {

    public static void main(String[] args) {
        try {
            // open websocket
            final WebsocketClientEndpoint clientEndPoint =
                    new WebsocketClientEndpoint(new URI("ws://localhost:8070/socket"));

            // add listener
            clientEndPoint.addMessageHandler(new IMessageHandler() {
                public void handleMessage(String message) {
                    System.out.println(message);
                }
            });

            // send message to websocket
            clientEndPoint.sendMessage("hello from Java client 3");


            // wait for messages from websocket
            Thread.sleep(15000);

        } catch (InterruptedException ex) {
            System.err.println("InterruptedException exception: " + ex.getMessage());
        } catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
        }
    }
}

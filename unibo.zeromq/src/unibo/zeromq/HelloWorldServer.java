package unibo.zeromq;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

public class HelloWorldServer{
    public static void main(String[] args) throws Exception{
    	System.out.println("HelloWorldServer STARTS " + Thread.activeCount());
 
        try (ZContext context = new ZContext()) {
            // Socket to talk to clients
            ZMQ.Socket socket = context.createSocket(ZMQ.REP);
            socket.bind("tcp://*:5555");

            while (!Thread.currentThread().isInterrupted()) {
                // Block until a message is received
                byte[] reply = socket.recv(0);

                // Print the message
                System.out.println(
                    "Received: [" + new String(reply, ZMQ.CHARSET) + "] " + + Thread.activeCount()
                );

                // Send a response
                String response = "Hello, world!";
                socket.send(response.getBytes(ZMQ.CHARSET), 0);
            }
        } 
    }
}
 

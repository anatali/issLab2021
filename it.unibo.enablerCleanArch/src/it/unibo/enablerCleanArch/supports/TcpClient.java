package it.unibo.enablerCleanArch.supports;
import java.net.Socket;
 
 
public class TcpClient {
 	
	public static Interaction2021 connect(String host, int port ) throws Exception {
		for( int i=1; i<=10; i++ ) {
			try {
		 		Socket socket           =  new Socket( host, port );
				Interaction2021 conn    =  new TcpConnection( socket );
				return conn;
			}catch(Exception e) {
				System.out.println("Another attempt to connect with host:" + host + " port=" + port);
				Thread.sleep(500);
			}
		}//for
		throw new Exception("Unable to connect to host:" + host);
 	}
  
}

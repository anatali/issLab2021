package it.unibo.enablerCleanArch.supports;
import java.net.Socket;
 
 
public class TcpClient {
		
	public static Interaction2021 connect(String host, int port, int nattempts ) throws Exception {
		for( int i=1; i<=nattempts; i++ ) {
			try {
		 		Socket socket         =  new Socket( host, port );
 				Interaction2021 conn  =  new TcpConnection( socket );
				return conn;
			}catch(Exception e) {
				Colors.out("TcpClient | Another attempt to connect with host:" + host + " port=" + port);
				Thread.sleep(500);
			}
		}//for
		throw new Exception("TcpClient | Unable to connect to host:" + host);
 	}
  
}

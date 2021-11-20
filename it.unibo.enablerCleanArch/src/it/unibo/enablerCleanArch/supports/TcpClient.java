package it.unibo.enablerCleanArch.supports;
import java.net.Socket;
 
 
public class TcpClient {
 	
	public static Interaction2021 connect(String host, int port ) throws Exception{
 		Socket socket           =  new Socket( host, port );
		Interaction2021 conn    =  new TcpConnection( socket );
		return conn;
 	}
  
}

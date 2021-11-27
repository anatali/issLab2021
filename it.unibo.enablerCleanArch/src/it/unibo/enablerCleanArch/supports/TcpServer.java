package it.unibo.enablerCleanArch.supports;

import java.net.ServerSocket;
import java.net.Socket;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
 
public class TcpServer {

	public TcpServer( String name, int port, ApplMessageHandler applHandler  ) {
		new Thread() {
			public void run() {
		      try {
			    ServerSocket serversock = new ServerSocket( port );
			    serversock.setSoTimeout(RadarSystemConfig.serverTimeOut);
				while( true ) {
					//Accept a connection				 
			 		System.out.println(name + " | waits on server port=" + port);	 
			 		Socket sock          = serversock.accept();	
			 		Interaction2021 conn = new TcpConnection(sock);
			 		applHandler.setConn(conn);
			 		//Create a message handler on the connection
			 		new TcpApplMessageHandler( applHandler );			 		
				}//while
			  }catch (Exception e) {
				  e.printStackTrace();
			  }	
			}
		}.start();
	}
 
	

}

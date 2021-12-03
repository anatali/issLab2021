package it.unibo.enablerCleanArch.supports;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
 
public class TcpServer extends Thread{
//private Thread executor;
private boolean stopped = true;
private ApplMessageHandler applHandler;
private int port;
private ServerSocket serversock;

	public TcpServer( String name, int port, ApplMessageHandler applHandler  ) {
		super(name);
		this.port        = port;
		this.applHandler = applHandler;
	      try {
		    serversock = new ServerSocket( port );
		    serversock.setSoTimeout(RadarSystemConfig.serverTimeOut);
		    //activate();
	     }catch (Exception e) { 
	    	 Colors.outerr(getName() + " | ERROR: " + e.getMessage());
	     }
	}
	
	@Override
	public void run() {
	      try {
			while( ! stopped ) {
				//Accept a connection				 
				Colors.out(getName() + " | waits on server port=" + port);	 
		 		Socket sock          = serversock.accept();	
		 		Interaction2021 conn = new TcpConnection(sock);
		 		applHandler.setConn(conn);
		 		//Create a message handler on the connection
		 		new TcpApplMessageHandler( applHandler );			 		
			}//while
		  }catch (Exception e) {
			  Colors.outerr(getName() + " | ERROR: " + e.getMessage());		 
		  }
	}
	
	public void activate() {
		if( stopped ) {
			stopped = false;
			this.start();
		}
	}
 
	public void deactivate() {
		try {
			stopped = true;
			serversock.close();
		} catch (IOException e) {
			System.out.println(getName() + " | ERROR: " + e.getMessage());	 
		}
	}
	

}

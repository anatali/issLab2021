package it.unibo.enablerCleanArch.supports;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
 
public class TcpServer extends Thread{
 
private boolean stopped = true;
private int port;
private ServerSocket serversock;
protected ApplMessageHandler applHandler;

	public TcpServer( String name, int port, ApplMessageHandler applHandler  ) {
		super(name);
	      try {
	  		this.port        = port;
			this.applHandler = applHandler;
		    serversock       = new ServerSocket( port );
		    serversock.setSoTimeout(RadarSystemConfig.serverTimeOut);
	     }catch (Exception e) { 
	    	 Colors.outerr(getName() + " | costruct ERROR: " + e.getMessage());
	     }
	}
	
	@Override
	public void run() {
	      try {
		  	//Colors.out(getName() + " | STARTING ... "  );
			while( ! stopped ) {
				//Accept a connection				 
				//Colors.out(getName() + " | waits on server port=" + port + " serversock=" + serversock, Colors.GREEN);	 
		 		Socket sock          = serversock.accept();	
				Colors.out(getName() + " | accept connection  handler=" + applHandler.name, Colors.GREEN  );	 
		 		Interaction2021 conn = new TcpConnection(sock);
		 		applHandler.setConn(conn);
		 		//Create a message handler on the connection
		 		new TcpApplMessageHandler( applHandler );			 		
			}//while
		  }catch (Exception e) {  //Scatta quando la deactive esegue: serversock.close();
			  Colors.out(getName() + " | probably socker closed: " + e.getMessage(), Colors.GREEN);		 
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
			Colors.out(getName()+" |  DEACTIVATE serversock=" +  serversock);
			stopped = true;
			serversock.close();
		} catch (IOException e) {
			Colors.outerr(getName() + " | deactivate ERROR: " + e.getMessage());	 
		}
	}
	

}

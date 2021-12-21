package it.unibo.enablerCleanArch.supports;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
 
public class TcpServer extends Thread{
//private int port;
private ServerSocket serversock;
protected IApplMsgHandler userDefHandler;
protected String name;
protected boolean stopped = true;

 	public TcpServer( String name, int port,  IApplMsgHandler userDefHandler   ) {
		super(name);
	      try {
	  		//this.port             = port;
	  		this.userDefHandler   = userDefHandler;
	  		Colors.out(getName() + " | costructor port=" + port  ); //+" applHandlerClass=" + applHandlerClass 
			this.name             = getName();
		    serversock            = new ServerSocket( port );
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
				//Colors.out(getName() + " | waits on server port=" + port + " serversock=" + serversock );	 
		 		Socket sock          = serversock.accept();	
				//Colors.out(getName() + " | accepted connection  "   );  
		 		Interaction2021 conn = new TcpConnection(sock);
		 		//Create a message handler on the connection
		 		new TcpApplMessageHandler( userDefHandler, conn );			 		
			}//while
		  }catch (Exception e) {  //Scatta quando la deactive esegue: serversock.close();
			  Colors.out(getName() + " | probably socket closed: " + e.getMessage(), Colors.GREEN);		 
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
		} catch (Exception e) {
			Colors.outerr(getName() + " | deactivate ERROR: " + e.getMessage());	 
		}
	}
 
}

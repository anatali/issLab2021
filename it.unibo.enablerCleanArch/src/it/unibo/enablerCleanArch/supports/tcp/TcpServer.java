package it.unibo.enablerCleanArch.supports.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;
 
public class TcpServer extends Thread{
private ServerSocket serversock;
protected IApplMsgHandler userDefHandler;
protected String name;
protected boolean stopped = true;

 	public TcpServer( String name, int port,  IApplMsgHandler userDefHandler   ) {
		super(name);
	      try {
	  		this.userDefHandler   = userDefHandler;
	  		ColorsOut.out(getName() + " | costructor port=" + port  );  
			this.name             = getName();
		    serversock            = new ServerSocket( port );
		    serversock.setSoTimeout(RadarSystemConfig.serverTimeOut);
	     }catch (Exception e) { 
	    	 ColorsOut.outerr(getName() + " | costruct ERROR: " + e.getMessage());
	     }
	}
	
	@Override
	public void run() {
	      try {
		  	ColorsOut.out(getName() + " | STARTING ... "  );
			while( ! stopped ) {
				//Accept a connection				 
				//ColorsOut.out(getName() + " | waits on server port=" + port + " serversock=" + serversock );	 
		 		Socket sock          = serversock.accept();	
				ColorsOut.out(getName() + " | accepted connection  "   );  
		 		Interaction2021 conn = new TcpConnection(sock);
		 		//Create a message handler on the connection
		 		new TcpApplMessageHandler( userDefHandler, conn );			 		
			}//while
		  }catch (Exception e) {  //Scatta quando la deactive esegue: serversock.close();
			  ColorsOut.out(getName() + " | probably socket closed: " + e.getMessage(), ColorsOut.GREEN);		 
		  }
	}
	
	public void activate() {
		if( stopped ) {
			stopped = false;
			this.start();
		}//else already activated
	}
 
	public void deactivate() {
		try {
			ColorsOut.out(getName()+" |  DEACTIVATE serversock=" +  serversock);
			stopped = true;
            serversock.close();
		} catch (Exception e) {
			ColorsOut.outerr(getName() + " | deactivate ERROR: " + e.getMessage());	 
		}
	}
 
}

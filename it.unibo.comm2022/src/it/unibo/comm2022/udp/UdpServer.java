package it.unibo.comm2022.udp;

import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import it.unibo.comm2022.interfaces.IApplMsgHandler;
import it.unibo.comm2022.interfaces.Interaction2021;
import it.unibo.comm2022.utils.ColorsOut;
import it.unibo.comm2022.utils.CommSystemConfig;
  
public class UdpServer extends Thread{
protected SocketUdpSupport udpSupport  ;
protected DatagramSocket serverSock;
protected int port;
protected IApplMsgHandler userDefHandler;
protected String name;
protected boolean stopped = true;

 	public UdpServer( String name, int port,  IApplMsgHandler userDefHandler   ) {
		super(name);
		this.port = port;
	      try {
	  		this.userDefHandler   = userDefHandler;
	  		ColorsOut.outappl(getName() + " | costructor port=" + port, ColorsOut.BLUE  );  
			this.name             = getName();
			udpSupport            = new SocketUdpSupport();
 	     }catch (Exception e) { 
	    	 ColorsOut.outerr(getName() + " | costruct ERROR: " + e.getMessage());
	     }
	}
	
	@Override
	public void run() {
	      try {
		  	ColorsOut.outappl(getName() + " | STARTING ... ", ColorsOut.GREEN  );
			while( ! stopped ) {
				//Accept a connection				 
				//ColorsOut.out(getName() + " | waits on server port=" + port + " serversock=" + serversock );	 
				serverSock = udpSupport.connectAsReceiver(port);
				ColorsOut.outappl(getName() + " | accepted connection  " + serverSock, ColorsOut.GREEN   );  
		 		Interaction2021 conn = new UdpConnection(serverSock,port,null);		 		
		 		//Create a message handler on the connection
		 		new UdpApplMessageHandler( userDefHandler, conn );			 		
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
			ColorsOut.out(getName()+" |  DEACTIVATE serverSock=" +  serverSock);
			stopped = true;
			serverSock.close();
		} catch (Exception e) {
			ColorsOut.outerr(getName() + " | deactivate ERROR: " + e.getMessage());	 
		}
	}
 
}

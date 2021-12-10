package it.unibo.enablerCleanArch.supports;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
 
public class TcpServer extends Thread{
 
private boolean stopped = true;
private int port;
private ServerSocket serversock;
protected ApplMessageHandler applHandler;
protected String name;

protected Class  applHandlerClass;

protected HashMap<String,ApplMessageHandler> handlerMap = new HashMap<String,ApplMessageHandler>();
protected HashMap<String,ApplMessage>    requestMap     = new HashMap<String,ApplMessage>();

	public TcpServer( String name, int port, ApplMessageHandler applHandler  ) {
		super(name);
	      try {
	  		this.port        = port;
			this.applHandler = applHandler;
			this.name        = getName();
		    serversock       = new ServerSocket( port );
		    serversock.setSoTimeout(RadarSystemConfig.serverTimeOut);
	     }catch (Exception e) { 
	    	 Colors.outerr(getName() + " | costruct ERROR: " + e.getMessage());
	     }
	}
	
	public TcpServer( String name, int port, String className   ) {
		super(name);
	      try {
	  		this.port             = port;
	  		Colors.out(getName() + " | costruct port " + port );
	  		this.applHandlerClass = Class.forName(className);
	  		Colors.out(getName() + " | costruct applHandlerClass " + applHandlerClass );
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
				Colors.out(getName() + " | waits on server port=" + port + " serversock=" + serversock, Colors.GREEN);	 
		 		Socket sock          = serversock.accept();	
				Colors.out(getName() + " | accept connection  handler=" + applHandler , Colors.GREEN  );	 
		 		Interaction2021 conn = new TcpConnection(sock);
		 		//applHandler.setConn(conn);
		 		Constructor c = applHandlerClass.getConstructor( it.unibo.enablerCleanArch.supports.Interaction2021.class  );
		 		ApplMessageHandler applHandle = (ApplMessageHandler) c.newInstance(conn);
				Colors.out(getName() + " | applHandle=" + applHandle , Colors.RED  );	 

		 		//Create a message handler on the connection
		 		new TcpApplMessageHandler( applHandle );			 		
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
	
	public void answer( String reqId,  String reqestmsgId, String caller, String  msg) {
		ApplMessage reqMsg = requestMap.remove( reqId+caller );  //Messaggio di richiesta
		Colors.out(name + " | answer finds:" + reqMsg);
		if( reqMsg == null ){
			Colors.outerr("INCONSISTENT: no request found");
		}else {
			String destName      = reqMsg.msgSender();
			ApplMessage replyMsg = new ApplMessage(reqMsg.msgId(), "reply", reqMsg.msgSender(), destName, msg, "1");
			Colors.out(name + " | answer replyMsg:" + replyMsg);
			Interaction2021 connToCaller = reqMsg.getConn();
			Colors.out(name + " | answer connToCaller:" + connToCaller, Colors.RED);
			try {
				connToCaller.forward( replyMsg.toString() );
			} catch (Exception e) {Colors.outerr("replyreq ERROR " + e.getMessage());	}
		}
	}

}

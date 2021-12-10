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
protected ApplMessageHandler userDefHandler;
protected String name;

protected Class  applHandlerClass;

protected HashMap<String,ApplMessageHandler> handlerMap = new HashMap<String,ApplMessageHandler>();
protected HashMap<String,ApplMessage>    requestMap     = new HashMap<String,ApplMessage>();
/*
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
*/	
	//public TcpServer( String name, int port, String className    ) {
	public TcpServer( String name, int port,  ApplMessageHandler userDefHandler   ) {
		super(name);
	      try {
	  		this.port             = port;
	  		this.userDefHandler   = userDefHandler;
	  		//Colors.out(getName() + " | costruct port " + port );
	  		//this.applHandlerClass = Class.forName(className);
	  		Colors.out(getName() + " | costructor port=" + port +" applHandlerClass=" + applHandlerClass  );
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
				Colors.out(getName() + " | accepted connection  handler class=" + applHandlerClass.getName()   );	 
		 		Interaction2021 conn = new TcpConnection(sock);
		 		//Constructor c = applHandlerClass.getConstructor( it.unibo.enablerCleanArch.supports.Interaction2021.class  );
		 		/*
		 		//Class[] parameterType = null;
		 		Constructor c = applHandlerClass.getConstructor(   );
		 		ApplMessageHandler applHandle = (ApplMessageHandler) c.newInstance();
		 		//Devo creare nuova istanza per memorizzare conn ma non devo perdere il rif a ILed 
		 		 * 
		 		 */
		 		SysMessageHandler hanlerWithConn = new SysMessageHandler(userDefHandler, conn);
		 		//hanlerWithConn.setConn(conn);
				//Colors.out(getName() + " | applHandle=" + applHandle  );	 

		 		//Create a message handler on the connection
		 		new TcpApplMessageHandler( hanlerWithConn );			 		
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

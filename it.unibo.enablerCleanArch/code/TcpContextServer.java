package it.unibo.enablerCleanArch.supports;

import java.util.HashMap;

import it.unibo.enablerCleanArch.domain.ApplMessage;

/*
 * Decorator
 */
public class TcpContextServer extends TcpServer{
//	protected HashMap<String,IApplMsgHandler> handlerMap = new HashMap<String,IApplMsgHandler>();
//	protected HashMap<String,ApplMessage>    requestMap  = new HashMap<String,ApplMessage>();
	
	public TcpContextServer(String name, int port, IApplMsgHandler handler) { //, IApplMsgHandler handler
		super(name, port, handler);
 	}
/*
	public void addComponent( String name, IApplMsgHandler h) {
		handlerMap.put(name, h);
	}
	public void removeComponent( String name ) {
		handlerMap.remove( name );
	}
*/
}

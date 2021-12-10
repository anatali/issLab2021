package it.unibo.enablerCleanArch.supports;

import java.util.HashMap;

import it.unibo.enablerCleanArch.domain.ApplMessage;

/*
 * Decorator
 */
public class TcpContextServer extends TcpServer{
//private static SysMessageHandler sysMsgHandler;

	public TcpContextServer(String name, int port, ApplMessageHandler handler ) {
		super(name, port, handler);
		//sysMsgHandler = new SysMessageHandler("sysMsgHandler");
		//sysMsgHandler = (SysMessageHandler) this.applHandler;
 	}
	/*		
	public static SysMessageHandler getSysHandler() {
		return (SysMessageHandler) sysMsgHandler;
	}

	public  SysMessageHandler getHandler() {
		return (SysMessageHandler) this.applHandler;  //inherited
	}	
*/	
	public void addComponent( String name, ApplMessageHandler h) {
		//sysMsgHandler.registerHandler(name,h);
		handlerMap.put(name, h);
	}
	public void removeComponent( String name ) {
		//sysMsgHandler.unregisterHandler(name );
		handlerMap.remove( name );
	}

	
}

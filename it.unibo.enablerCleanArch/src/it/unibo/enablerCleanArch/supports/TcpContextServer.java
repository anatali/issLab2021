package it.unibo.enablerCleanArch.supports;
 

public class TcpContextServer extends TcpServer{
private SysMessageHandler sysMsgHandler;

	public TcpContextServer(String name, int port ) {
		super(name, port, new SysMessageHandler("sysMsgHandler"));
		sysMsgHandler = getHandler();
 	}
	
	public SysMessageHandler getHandler() {
		return (SysMessageHandler) applHandler;
	}
	
	public void addComponent( String name, ApplMessageHandler h) {
		sysMsgHandler.registerHandler(name,h);
	}
	public void removeComponent( String name, ApplMessageHandler h) {
		sysMsgHandler.unregisterHandler(name );
	}

	
}

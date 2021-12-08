package it.unibo.enablerCleanArch.supports;
 

public class TcpContextServer extends TcpServer{

	public TcpContextServer(String name, int port ) {
		super(name, port, new SysMessageHandler("sysHandler"));
 	}
	
	public SysMessageHandler getHandler() {
		return (SysMessageHandler) applHandler;
	}
 
}

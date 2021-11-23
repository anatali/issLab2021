package it.unibo.enablerCleanArch.enablers;
import it.unibo.enablerCleanArch.supports.ApplMessageHandler;

public abstract class EnablerAsServer extends ApplMessageHandler{
	public EnablerAsServer(String name, int port) {
		super(name);
		try {
			setProtocolServer( port, this );
		} catch (Exception e) {
			System.out.println(name+" ERROR " + e.getMessage() );
		} 			
	}	
	public abstract void setProtocolServer(int port, ApplMessageHandler handler) throws Exception;    	
	@Override
	public abstract void elaborate(String message);
}

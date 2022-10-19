package it.unibo.enablerCleanArch.enablers;
import it.unibo.enablerCleanArch.supports.ApplMessageHandler;

public abstract class EnablerAsServer extends ApplMessageHandler{
	public EnablerAsServer(String name, int port) {
		super(name);
		try {
			setProtocolServer( port  );
		} catch (Exception e) {
			System.out.println(name+" ERROR " + e.getMessage() );
		} 			
	}	
    public abstract void setProtocolServer( int port ) throws Exception; 
	@Override
	public abstract void elaborate(String message);
}

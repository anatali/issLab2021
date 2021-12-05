package it.unibo.enablerCleanArch.enablers;
import it.unibo.enablerCleanArch.supports.ApplMessageHandler;
import it.unibo.enablerCleanArch.supports.Colors;

public abstract class EnablerAsServer extends ApplMessageHandler{
	public EnablerAsServer(String name, int port) {
		super(name);
		try {
			setServerSupport( port  );
		} catch (Exception e) {
			Colors.outerr(name+" ERROR " + e.getMessage() );
		} 			
	}	
    public abstract void setServerSupport( int port ) throws Exception; 
 
}

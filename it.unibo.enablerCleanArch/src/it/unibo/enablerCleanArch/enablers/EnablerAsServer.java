package it.unibo.enablerCleanArch.enablers;
import it.unibo.enablerCleanArch.supports.ApplMessageHandler;
import it.unibo.enablerCleanArch.supports.Colors;

public abstract class EnablerAsServer {
	protected String name;
	
	public EnablerAsServer(String name, int port, ApplMessageHandler handler) {
		try {
			this.name = name;
			setServerSupport( port,handler  );
		} catch (Exception e) {
			Colors.outerr(name+" ERROR " + e.getMessage() );
		} 			
	}	
    public abstract void setServerSupport( int port, ApplMessageHandler handler ) throws Exception; 
    
 	//@Override  //from ApplMessageHandler
	//public abstract void elaborate(String message) ;
 
}

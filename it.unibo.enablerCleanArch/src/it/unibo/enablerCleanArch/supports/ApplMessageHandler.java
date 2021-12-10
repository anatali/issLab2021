package it.unibo.enablerCleanArch.supports;

import java.util.HashMap;

import it.unibo.enablerCleanArch.domain.ApplMessage;

/*
 * 
 */
public abstract class ApplMessageHandler {  
private static int count = 1;
protected Interaction2021 conn;		//Injected
protected String name;

protected HashMap<String,Interaction2021> connMap = new HashMap<String,Interaction2021>();
 

	public ApplMessageHandler( String name ) {
		this.name = name;
	}
  
	public ApplMessageHandler( Interaction2021 conn ) {
		this.name = "applmsgh"+count++;
		this.conn = conn;
	}
	
	public abstract void elaborate( String message ) ;
	
	/* Used in case of reply Called by the server at a new connection
	public void addConn( Interaction2021 conn) {
		//Colors.out("ApplMessageHandler | setConn:" + conn, Colors.BLUE);
		 connMap.put("aa",conn);
	}
	*/
	public String getName() {
		return name;
	}
	
	public Interaction2021 getConn(  ) {
		return conn;
	}
 	
}

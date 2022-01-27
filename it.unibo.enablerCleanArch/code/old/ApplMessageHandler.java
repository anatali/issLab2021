package it.unibo.enablerCleanArch.supports;

import java.util.HashMap;
import it.unibo.enablerCleanArch.domain.ApplMessage;

/*
 * 
 */
public abstract class ApplMessageHandler {  
protected static int count = 1;
protected Interaction2021 conn;		//Injected
protected String name;
 

//protected HashMap<String,Interaction2021> connMap = new HashMap<String,Interaction2021>();
 

	public ApplMessageHandler(   ) {  
		this.name = "appl_msgh_"+count++; 
		Colors.out(name + " | CREATED", Colors.ANSI_YELLOW);
	}
	public ApplMessageHandler( String name  ) {  
		this.name = name;
		Colors.out(name + " | CREATING ... " , Colors.ANSI_PURPLE);
	}
  
	/*
	public ApplMessageHandler( Interaction2021 conn ) { //OBSOLETO
		this.name = "applmsgh"+count++;
		this.conn = conn;
	}
	*/
	public void setConn( Interaction2021 conn  ) {
		Colors.out(name + " | setConn:" + conn, Colors.ANSI_YELLOW);
		this.conn = conn;
	}
	public abstract void elaborate(String message) ;
	public abstract void elaborate( String message, Interaction2021 conn ) ;
	
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

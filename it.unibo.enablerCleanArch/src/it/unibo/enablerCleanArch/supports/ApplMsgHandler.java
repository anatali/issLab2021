package it.unibo.enablerCleanArch.supports;


/*
 * 
 */
public abstract class ApplMsgHandler  implements IApplMsgHandler {  
protected String name;
   
 	public ApplMsgHandler( String name  ) {  
		this.name = name;
		Colors.out(name + " | CREATING ... " , Colors.ANSI_PURPLE);
	}
   		
 	public void sendMsgToClient( String message, Interaction2021 conn  ) {
 		try {
 			Colors.out(name + " | sendMsgToClient " + message);
			conn.forward( message );
		} catch (Exception e) {
 			Colors.outerr(name + " | ERROR " + e.getMessage());;
		}
 	} 
 	public abstract void elaborate(String message, Interaction2021 conn) ;
 	
 	public String getName() {
		return name;
	}	
}

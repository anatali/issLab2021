package it.unibo.enablerCleanArch.supports;

import it.unibo.enablerCleanArch.supports.coap.CoapDeviceResource;

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
			conn.forward( message );
		} catch (Exception e) {
 			e.printStackTrace();
		}
 	}
 	public abstract void elaborate(String message, Interaction2021 conn) ;
 	
 	public String getName() {
		return name;
	}	
}

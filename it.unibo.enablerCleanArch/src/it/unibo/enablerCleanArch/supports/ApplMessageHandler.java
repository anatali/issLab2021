package it.unibo.enablerCleanArch.supports;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;

/*
 * 
 */
public abstract class ApplMessageHandler implements CoapHandler{  
protected Interaction2021 conn;		//Injected
protected String name;

	public ApplMessageHandler( String name ) {
		this.name = name;
	}
 
	public abstract void elaborate( String message ) ;
	
	/* Used in case of reply */
	public void setConn( Interaction2021 conn) {
		System.out.println("ApplMessageHandler | setConn:" + conn);
		this.conn = conn;
	}
	
	public String getName() {
		return name;
	}
	
	public Interaction2021 getConn(  ) {
		return conn;
	}

//--- from CoapHandler
	
	@Override
	public void onLoad(CoapResponse response) {
		String msg    = response.getResponseText();
		System.out.println(name + "   |  onLoad msg= " + msg);		
		elaborate( msg );
	}
	
	
	@Override
	public void onError() {
		System.out.println(name + "   |  onError  ");
		
	}

	
	
}
